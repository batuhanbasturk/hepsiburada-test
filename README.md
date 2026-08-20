# hepsiburada-test

Hepsiburada üzerinde giriş yapma, ürün arama ve sepete ekleme akışını test eden
[Gauge](https://gauge.org) + [Selenium](https://www.selenium.dev) + Java test otomasyon projesi.

## Gereksinimler

- JDK 26 (veya üzeri)
- [Maven](https://maven.apache.org)
- [Gauge CLI](https://docs.gauge.org/getting_started/installing-gauge.html) ve `gauge-java` plugin'i (`gauge install java`)
- Google Chrome (testler ChromeDriver ile çalışır)

## Kurulum

1. Bağımlılıkları indirip derleyin:

   ```
   mvn compile test-compile
   ```

   Bu adım `gauge-java` plugin'inin ihtiyaç duyduğu jar'ları `libs/` klasörüne kopyalar.

2. Giriş bilgilerini tanımlayın. Gerçek kullanıcı adı/şifre git'e commit'lenmesin diye
   `src/test/resources/config/credentials.json` `.gitignore`'dadır. Örnek dosyayı kopyalayıp
   kendi değerlerinizi girin:

   ```
   cp src/test/resources/config/credentials.example.json src/test/resources/config/credentials.json
   ```

   `credentials.json` içindeki `USERNAME` ve `PASSWORD` değerlerini gerçek test hesabıyla değiştirin.

## Çalıştırma

Tüm spec'leri çalıştırmak için proje kök dizininde:

```
gauge run specs
```

Tek bir spec dosyasını çalıştırmak için:

```
gauge run specs/hb_login_arama_sepet.spec
```

Yalnızca belirli bir tag ile çalıştırmak için (`specs/hb_login_arama_sepet.spec` içindeki
`Tags: login, search, cart` etiketlerine göre):

```
gauge run --tags "cart" specs
```

Çalıştırma sonrası HTML raporu `reports/html-report/index.html` altında oluşur.

## Proje Yapısı

```
├── specs/
│   ├── hb_login_arama_sepet.spec       Uçtan uca senaryo: login → arama → sepete ekleme
│   └── concepts/
│       ├── login.cpt                   "E-posta ve şifre ile giriş yap" concept'i
│       ├── search.cpt                  Ürün arama ve ürün sayfasına gitme concept'i
│       └── cart.cpt                    Sepete ekleme ve doğrulama concept'i
│
├── src/main/java/                      Gauge'un beklediği boş Main sınıfı (proje iskeleti)
│
├── src/test/java/com/hepsiburada/
│   ├── config/
│   │   ├── ElementDefinition.java      JSON'daki {key, value, type} kaydının Java karşılığı
│   │   └── ElementRepository.java      elements.json + credentials.json'u okuyup key'den
│   │                                   Selenium By/locator ya da düz değer üreten repository
│   │                                   (websiteURL, bekleme süresi gibi genel ayarlar da burada)
│   │
│   ├── driver/
│   │   └── DriverFactory.java          ChromeDriver oluşturma/kapatma, sekme geçişi
│   │
│   ├── hooks/
│   │   └── DriverHooks.java            Her senaryo öncesi/sonrası driver aç/kapat, hata anında ekran görüntüsü alma (Gauge hook)
│   │
│   ├── pages/                          Page Object sınıfları (BasePage'den türer)
│   │   ├── BasePage.java               Element key'i ile çalışan generic aksiyonlar:
│   │   │                               click, enterText, textOf, isElementVisible, counterValue...
│   │   ├── HomePage.java               Anasayfaya özgü akışlar (çerez banner'ı, arama kutusu)
│   │   ├── LoginPage.java              Giriş sayfası işaretleyicisi (aksiyonlar BasePage'den)
│   │   ├── ProductPage.java            Ürün sayfası işaretleyicisi
│   │   ├── SearchResultsPage.java      Arama sonucu grid'inden ürün bulma (satır/sütun index'i)
│   │   └── CartPage.java               Sepetteki ürünleri kontrol etme
│   │
│   └── steps/                          Gauge step tanımları (.spec/.cpt dosyalarındaki adımlar)
│       ├── ElementSteps.java           Genel, key parametreli step'ler (tıkla, görünürlük doğrula)
│       ├── LoginSteps.java             E-posta/şifre girme step'leri
│       ├── SearchSteps.java            Arama, ürün seçme, başlık doğrulama step'leri
│       └── CartSteps.java              Sepet sayacı/ürün doğrulama step'leri
│
├── src/test/resources/
│   └── config/
│       ├── elements.json               Tüm element seçicileri (locator) ve genel config
│       │                               değerleri: { "key", "value", "type" } listesi
│       ├── credentials.example.json    credentials.json için şablon (commit'lenir)
│       └── credentials.json            Gerçek kullanıcı adı/şifre (commit'lenmez, .gitignore'da)
│
├── env/default/                        Gauge'un ortam ayarları (rapor/log klasörleri vb.)
├── manifest.json                       Gauge proje tanımı (dil: java, plugin: html-report)
└── pom.xml                             Maven bağımlılıkları ve derleme ayarları
```

## Element Repository Mantığı

Sayfalardaki hiçbir CSS seçici veya id Java kodunda sabit (hardcoded) yazılmaz. Bunun yerine:

1. `config/elements.json` içinde her element bir **key/value/type** kaydı olarak tanımlanır, örn:

   ```json
   { "key": "USERNAME_INPUT", "value": "txtUserName", "type": "id" }
   ```

2. Page/Step sınıfları elementi bu **key** ile ister (`waitVisible("USERNAME_INPUT")` gibi);
   gerçek seçiciyi `ElementRepository` çözer.

3. `.spec` ve `.cpt` dosyalarında da bu key'ler adım parametresi olarak görünür
   (`"USERNAME_INPUT" alanına e-posta adresini gir` gibi), böylece bir elementin seçicisi
   değiştiğinde sadece `elements.json` güncellenir — Java koduna ya da spec/concept
   dosyalarına dokunmaya gerek kalmaz.

`CAPTURED_TITLE`, `CART_COUNT_BEFORE_ADD` gibi bazı key'ler ise gerçek bir DOM elementi değil,
senaryo içinde adımlar arası veri taşımak için kullanılan `ScenarioDataStore` anahtarlarıdır.
