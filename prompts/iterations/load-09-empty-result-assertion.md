# Boş arama sonucu assertion önerisi

Bu görüşmedeki teknik isteğe dayanan kayıt. Kaynakta doğrulanabilir ayrı bir mesaj zaman damgası bulunmadığı için tarih/saat eklenmedi. Kayıt sonradan derlenmiştir. Amaç, bağlam, çıktı değerlendirmesi ve iterasyon notları geriye dönük özetlerdir; düzenlenmiş istek metniyle birlikte sunulur.

## 1. Amaç

Sonuçsuz aramanın beklenen yanıtını doğrulamak.

## 2. Verilen bağlam

Paylaşılan HTML’de emptySearchTitle sınıflı “Aradığını bulamadık.” mesajı ve ayrıca önerilen ürünler bulunuyordu.

## 3. İsteğin düzenlenmiş anlatımı

Aşağıdaki metin, teknik isteğin sonradan düzenlenmiş anlatımıdır; gönderilen mesajın birebir kopyası değildir. Ekran görüntüsü/HTML varsa bağlam bölümünde açıklanır; özel dosya yolları veya ham yanıtın tamamı teslim kaydına taşınmaz.

~~~~text
non data response için nasıl doğrulama yapabilirim burada gelen hangi data doğrulama için faydalı olur ?
~~~~

## 4. Çıktı değerlendirmesi

Asistan boş sonuç mesajının p etiketi içindeki varlığını kontrol eden regex önerdi. Önerilen ürünler bulunduğu için sayfada hiç ürün kartı olmamasını beklemek reddedildi. HTTP 200 kontrolü ayrı tutuldu.

## 5. İterasyon notları

Sonraki mesajda aday hangi ayarları seçip nasıl ekleyeceğini sordu.

Sonraki kayıt: [Boş sonuç assertion ayarlarının uygulanması](load-10-empty-result-settings.md).
