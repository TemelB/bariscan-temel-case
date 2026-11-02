# JMeter çalışmasında AI desteği

Bu dosya görev özetidir; özgün prompt değildir. İlgili teknik istek ve değerlendirme kayıtları, her iterasyon için ayrı dosya olacak şekilde [iterations/](iterations/) altında tutulur.

## Amaç ve bağlam

Case study için tek kullanıcılı n11 arama planı, HTTP 403 teşhisi ve arama yanıtlarının doğrulanması üzerinde çalışıldı. Aday güncel planı JMeter arayüzünde manuel yeniden hazırladı; teşhis ve assertion seçiminde AI desteği aldı.

## Süreç ve değerlendirme

İlk plan, başarısız istek incelemesi, header mesajının açıklanması, 403/koruma olasılığı ve belgeleme ayrı kayıtlarda yer alır. Kesin engelleme kuralı kanıtlanmadı; 403 başarılı sayılmadı. Daha sonra adayın paylaştığı HTML ve ekran görüntüleri üzerinden laptop başlığı/ürün kartı ile boş sonuç mesajı assertion’ları önerildi. Aday ayarları uygulayıp dosyayı kaydetti; yanlış Response Message seçimi Text Response olarak düzeltildi.

## Güncel sonuç

Teslim JMX’i üç istek, HTTP 200 assertion’ları ve iki arama yanıtında içerik assertion’ları içerir. XML ve paylaşılan HTML üzerinden pattern kontrollerinden sonra 2 Kasım 2026 tarihinde güncel plan CLI üzerinden çalıştırıldı: üç istekten ikisi geçti, ana sayfa 403 nedeniyle başarısız kaldı. Çok satırlı CSV mesajlarının launcher sayımını bozması düzeltildi; aynı JTL ile 3 örnek/1 hata sonucu dashboard ile eşleşti. JMX değiştirilmedi. Ayrıntılar [JMeter README](../load-tests/README.md) ve [çalıştırma notlarında](../docs/EXECUTION.md) bulunur.
