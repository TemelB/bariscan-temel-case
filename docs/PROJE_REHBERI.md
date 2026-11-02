# Projeyi nereden okuyacağım?

Önce `src/test/resources/features` klasörünü aç. Senaryolar burada İngilizce Given / When / Then formatında yazılı.

## 1. Senaryo: neyi test ediyoruz?

- `features/ui/careers.feature`: Ana sayfa, QA ilanları, Sales Operations alternatif başvuru akışı ve takım kartlarını açıp kapatma.
- `features/api/pet_crud.feature`: Bağımsız CRUD, form-data güncelleme ve status ile arama dahil altı pozitif senaryo.
- `features/api/pet_negative.feature`: Yedi negatif API senaryosu. Senaryolar API grubunda çalışır.

API senaryoları artık ayrı `Scenario` bloklarıdır. Tek senaryo üzerinde çalışmak için `@wip` etiketini o senaryonun üstüne taşı. Read, Update ve Delete kendi ön koşul kaydını oluşturur; başka bir senaryonun önce çalışmasına ihtiyaç duymaz.

## 2. Step definition: adımın Java karşılığı ne?

`stepdefs/CareersStepdefs.java` UI adımlarını, `stepdefs/PetStepdefs.java` API hazırlık, pet işlemleri ve assertion adımlarını karşılar. IntelliJ'de Given / When / Then satırından Java metoduna geçebilirsin. Önce Maven importunun tamamlanmasını bekle. Cucumber for Java desteği kapalıysa feature dosyasından doğrudan çalıştırma/navigasyon kullanılamayabilir; Maven komutları yine çalışır.

Örneğin `When I retrieve my pet` → `PetStepdefs.whenIRetrieveMyPet()` → `PetClient.get()` → HTTP GET isteği. Feature dosyaları pet oluşturma, güncelleme ve silme gibi davranışları anlatır; endpoint ve payload ayrıntıları Java tarafında tutulur. Request/response logları, bağımsız test verisi ve temizlik korunur.

## 3. Pages ve clients: teknik işlem nerede?

- `ui/pages`: Selenium locator'ları, tıklamalar, beklemeler ve sayfa kontrolleri.
- `clients/PetClient.java`: REST Assured ile HTTP istekleri.
- `context`: Aynı senaryonun adımları arasında paylaşılan browser/response/pet ID bilgisi. Her senaryo ayrı context alır.
- `hooks`: ilk UI adımında Chrome açılır, senaryo sonrasında kapanır; API sonrasında yalnızca testin oluşturduğu kayıtlar temizlenir.

## 4. Kalıcı tag’ler ve @wip

Senaryoların kalıcı, anlamlı tag’leri var:

- Feature: `@api @pet @positive`, `@api @pet @negative` veya `@ui @careers`.
- Senaryo: `@createPet`, `@readPet`, `@updatePet`, `@deletePet` gibi ne yaptığını anlatan tag’ler.
- Öncelik: `@p1` / `@p2`.
- Ek UI senaryosunda `@aiGenerated`; sözleşme kontrollerinde `@contract`.

Bunlar yerinde kalır. Üzerinde çalışmak istediğin yere ayrıca **@wip** eklersin:

```gherkin
@api @pet @positive
Feature: Petstore pet CRUD operations

  @p1 @createPet @wip
  Scenario: Create a pet and verify it is persisted
```

```bash
mvn test -Dcucumber.filter.tags="@wip"
```

Bu örnekte yalnızca Create seçilir. Bir feature dosyasındaki bütün senaryoları seçmek için @wip etiketini kalıcı feature tag’lerinin yanına, Feature satırının üstüne taşı. Yalnızca bir senaryoyu seçmek için feature veya diğer senaryolarda @wip bırakma. Kalıcı tag’leri silmene gerek yok; @wip filtresi yalnızca @wip işaretli yerleri seçer.

İstersen doğrudan anlamlı tag ile de seçebilirsin:

```bash
mvn test -Dcucumber.filter.tags="@createPet"
mvn test -Dcucumber.filter.tags="@readPet"
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui"
```

Etiket filtresi olmadan hepsi için `mvn test`. IntelliJ’de senaryonun yeşil çalıştırma ikonu doğrudan seçim sağlar; runner konfigürasyonunun VM options alanında @wip filtresi de kullanılabilir.

CRUD dört bağımsız senaryodur. Her biri kendi verisini hazırlar. API çalışırken tarayıcı açılmaz; ilk UI adımı gerektiğinde Chrome başlar. Hazırlık ve temizlik, seçim tag’lerinden bağımsızdır.

Rapor `target/cucumber-report.html`, özet `target/scenario-summary.txt` dosyasındadır. Terminalde yalnızca seçilen case’lerin CASE RESULT ve SCENARIO SUMMARY bilgileri gösterilir; Maven’ın keşfedilmiş bütün senaryoları sayan özeti kapalıdır. API case’i bitince request/response kayıtları da görünür. XML raporları filtre dışı senaryoları ayrıca içerebilir. JMeter planı `load-tests/n11-search.jmx` dosyasında ayrı çalışır.

Bilinen canlı servis ve tarayıcı sorunları EXECUTION.md / KNOWN_ISSUES.md içinde tutulur.

Ek çalıştırma: `mvn verify -Pload` yalnızca JMeter planını çalıştırır; Cucumber senaryoları bu profilde atlanır. JMeter 5.6.3 Maven tarafından indirilir. Tarayıcı varsayılan olarak headless çalışır; görünür Chrome için `-Dheadless=false` kullan. Bilinen sorunlar varsayılan çalıştırmadan gizlenmez.


## Allure raporu

Temiz bir seçim için `mvn clean test -Dcucumber.filter.tags="@api"` çalıştır. Testler başarısız olsa da rapor üretmek için ardından `mvn allure:report`, görüntülemek için `mvn allure:serve` kullan. Sonuçlar `target/allure-results/`, HTML rapor `target/site/allure-maven-plugin/` altındadır; ikisi de Git dışında kalır. `clean` kullanmadan yapılan koşumlar önceki sonuçlarla birleşir. API trafik ekleri ve mevcut UI hata ekran görüntüleri rapora taşınır; JMeter kendi dashboard’unu kullanır.


## Step definition sorumlulukları

Step definition’lar senaryo adımını ilgili işleme bağlar. Kullanılmamış ID bulma ve mevcut pet hazırlama `fixtures/PetFixture.java`, HTTP/payload ayrıntıları `clients/PetClient.java`, senaryoya ait ID ve yanıt `context/PetContext.java` içinde tutulur. Örneğin ID adımı yalnızca `context.setPetId(fixture.findUnusedId())` çağırır. Rastgele ID serviste kontrol edilir; çakışma varsa en fazla üç aday denenir, beklenmeyen servis hatası gizlenmeden başarısız olur.
