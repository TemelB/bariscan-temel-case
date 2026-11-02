# Ekip kartlarını açma, kapatma ve yeniden açma

Bu dosya görev özetidir. Gerçek mesajlara dayanan ayrı iterasyon kayıtları: [ilk kayıt](iterations/ui-01-initial-implementation.md); tüm kayıtlar için [dizin](README.md).

## 1. Amaç

Careers sayfasındaki ekip kartlarının açılma, kapanma ve yeniden açılma davranışını doğrulayan, ana başvuru akışından bağımsız bir UI senaryosu oluşturmak istedim.

## 2. Verdiğim bağlam

Java, Maven, Selenium, Cucumber ve JUnit kullandığım proje yapısını, case study gereksinimlerini ve mevcut Careers Page Object sınıflarını bağlam olarak sundum. Ek UI senaryosu case study PDF’inin kapsamında AI tarafından önerildi; başlangıçta doğrudan ekip kartlarını isteyen ayrı bir mesaj göndermedim.

## 3. Prompt

### Teknik isteklerimin düzenlenmiş anlatımı

Aşağıdaki metinleri, bu görüşmede ilettiğim teknik istekleri daha açık ve profesyonel anlatmak için AI desteğiyle düzenledim. Bunlar özgün mesajların birebir alıntısı veya bütün iterasyonların eksiksiz kaydı değildir.

**Başlangıç bağlamı:** Case study PDF’i bağlamdayken testlerin yazılmasını istedim; AI, ek UI senaryosunu bu kapsamda önerdi.

**Kapsamı yeniden değerlendirme isteğim:**

> ui senaryolarını da case study'e göre review eder misin ?

### AI desteğiyle hazırladığım görev promptu

Önce gereksinimlerimi ve mevcut bağlamı AI’a aktarıyorum; ardından bu bilgilerle daha açık bir görev promptu hazırlatıyorum. Hazırlanan metni kendim değerlendiriyor, gerekli yönlendirmeleri yapıyor ve çıktıyı kontrol ediyorum. Aşağıdaki görev promptu da bu yöntemle, geliştirme sırasında netleşen beklentilerimden derlendi; ilk uygulama öncesinde aynen gönderilmiş bir mesaj olarak sunulmuyor.

~~~~text
Mevcut Java, Maven, Selenium, Cucumber ve JUnit projesindeki Careers sayfasını incele. Ekip kartlarının açılma, kapanma ve yeniden açılma davranışını doğrulayan bağımsız bir UI senaryosu oluştur.

Beklentiler:
- Ekipler genişletilmeden önce görünen kartları kaydet.
- “See all teams” düğmesine basıldığında daha fazla kartın görünmesini doğrula.
- Görünen ekiplerin benzersiz olduğunu; her kartta boş olmayan başlık, ilan sayısı ve geçerli Lever bağlantısı bulunduğunu kontrol et.
- Açık ilanı bulunan ekiplerin bağlantılarında doğru departman filtresinin korunduğunu doğrula. İlanı olmayan ekiplerde pozitif ilan sayısı bekleme.
- Ekipler kapatıldığında yalnızca başlangıçta görünen kartların kalmasını doğrula.
- Yeniden açıldığında ilk genişletmedeki kartların aynı sırayla göründüğünü kontrol et.

Mevcut Page Object yapısını ve regex tabanlı step definition biçimini koru. Sabit bekleme yerine duruma bağlı explicit wait kullan. Senaryo adı “Verify that” ile başlasın; @teamsToggle, @aiGenerated ve @positive taglerini kullan. Geçici @wip ekleme.

Değişiklikleri doğrula; kabul ettiğin ve reddettiğin yaklaşımları gerekçeleriyle açıkla. Çalıştıramadığın kontrolleri başarılı olarak raporlama. Commit veya push yapma.
~~~~

## 4. Çıktıyı değerlendirmem

İlk sürümün ana akıştan bağımsız olmasını ve açma/kapatma durumunu explicit wait ile kontrol etmesini uygun buldum. Ancak yalnızca QA kartının görünürlüğünün kontrol edilmesini kapsam açısından yetersiz buldum; sonraki incelemede bütün kartların benzersizliği, başlıkları, ilan sayıları ve Lever bağlantıları da kontrol edilecek şekilde senaryo genişletildi. Kart başlığı ile departman kimliğini birebir aynı kabul etmek yerine başlığın boş olmaması ve doğru bağlantının bulunması esas alındı; çünkü sayfadaki gösterim adları departman adlarından farklı olabiliyor.

## 5. İterasyon notlarım

**İlk sürüm:** PDF’teki ek UI gereksinimine dayanarak ekipleri açan, kapatan ve yeniden açan senaryo üretildi. Bu sürümde QA kartının görünürlüğü ve tekrar etmemesi kontrol ediliyordu; senaryoyu kendim çalıştırıp geçtiğini bildirdim.

**İnceleme ve iyileştirme:** Kapsamı yeniden değerlendirdim. Sonraki incelemede kontrol bütün ekip kartlarına genişletildi; başlık, ilan sayısı, Lever bağlantısı, benzersizlik ve kapatma sonrasında başlangıç durumuna dönüş doğrulandı. Kartın görünen başlığının departman kimliğiyle her zaman aynı olmadığı dikkate alındı.

**Doğrulama:** İlk asistan denemesinde Chrome oturumu başlatılamadı. Sonrasında kendi terminalimde Sales Operations ve genişletilmiş ekip kartı senaryosunu birlikte çalıştırdım; iki senaryo da geçti. O aday koşumuna ait Cucumber JSON raporunda Sales Operations senaryosunun dokuz, ekip kartı senaryosunun yedi adımı başarılıydı. Sonraki asistan koşumlarında Chrome oturumu başlatılamadı; güncel review sonucu [son UI incelemesinde](iterations/ui-03-final-task-review.md) ayrıca belirtilmiştir. Bu sonuç QA ilanı gerektiren ayrı senaryonun geçtiği anlamına gelmiyor.
