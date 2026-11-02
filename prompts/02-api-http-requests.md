# Davranış odaklı API test senaryoları

Bu dosya mevcut API tasarımının görev özetidir. Aşağıdaki derlenmiş görev promptu, geçmişte birebir gönderilmiş bir mesaj olarak sunulmaz. Güncel değerlendirme: [BDD senaryolarının review kaydı](iterations/api-01-current-bdd-review.md); diğer kayıtlar için [dizin](README.md).

## 1. Amaç

API senaryolarının HTTP metodu, endpoint ve JSON ayrıntıları yerine pet oluşturma, getirme, güncelleme ve silme davranışlarını anlatmasını istedim. Teknik request ayrıntılarını Java tarafında tutarak feature dosyalarının okunabilirliğini artırmayı hedefledim.

## 2. Verdiğim bağlam

Java, Maven, REST Assured, Cucumber ve JUnit kullanan projeyi ve mevcut pozitif/negatif API senaryolarını bağlam olarak sundum. Bağımsız fixture oluşturma, benzersiz pet ID, yalnızca senaryonun oluşturduğu kayıtların temizlenmesi ve request/response loglarının korunmasını istedim.

## 3. Görev promptunun düzenlenmiş özeti

Bu metin güncel tasarım isteğinin sonradan derlenmiş teknik anlatımıdır; özgün mesajın birebir alıntısı değildir.

~~~~text
Mevcut API feature dosyalarını davranış odaklı BDD adımlarıyla düzenle. Senaryolar pet oluşturma, getirme, güncelleme, silme, form verisiyle güncelleme ve duruma göre arama işlemlerini anlatsın. HTTP metodu, endpoint ve request body ayrıntılarını Java tarafında tut.

Örnek kullanım:
Given an existing pet named 'update-fixture' with status 'available'
When I update my pet to name 'case-pet-updated' and status 'sold'
Then the response status should be 200
And the pet response should have name 'case-pet-updated' and status 'sold'
When I retrieve my pet
Then the response status should be 200
And the pet response should have name 'case-pet-updated' and status 'sold'

Mevcut regex biçimini, sayısal status parametrelerini, senaryo taglerini ve yanıt kontrollerini koru. Her senaryo kendi verisini hazırlasın; temizlik yalnızca ilgili senaryonun oluşturduğu kayıtları kapsasın. HTTP istemcisi, timeout ve loglama Java tarafında ortak kalabilsin.

Negatif adımlar geçersiz ID veya bozuk JSON davranışını açıkça anlatsın; bilerek bozuk gönderilen JSON serializer tarafından düzeltilmesin. Kullanılmayan genel HTTP step definition’larını kaldır. README ve proje rehberindeki kullanım örneklerini yeni yapıyla eşleştir. Derleme, adım eşleşmeleri ve canlı koşum sonuçlarını birbirinden ayırarak raporla.
~~~~

## 4. Çıktı değerlendirmesi

Feature dosyalarında pet işlemlerini anlatan adımlar kullanılıyor; payload ve HTTP ayrıntıları Java tarafında oluşturuluyor. Genel request step sınıfı kaldırıldı; REST Assured istemcisi, bağımsız test verisi, cleanup ve request/response logları korundu. Negatif senaryolarda bozuk JSON ham metin olarak gönderiliyor; form güncelleme ve duruma göre arama kapsamı korunuyor.

## 5. İterasyon notları

Güncel API tasarımı, pet işlemlerini anlatan BDD adımlarını ve Java tarafında ortak istemci/fixture kullanımını esas alır. Dönüşüm sonrası 16 senaryonun dry-run kontrolü geçti; sonrasında Allure entegrasyonu ve gerekli JSON serializer bağımlılığıyla yapılan canlı API koşumunda 13 senaryonun tamamı başarılı oldu. Güncel kapsam ve koşum ayrıntıları [çalıştırma notlarında](../docs/EXECUTION.md) tutulur.
