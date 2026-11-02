# AI iş birliği kayıtları

Görev özetleri güncel tasarımı ve değerlendirmeyi anlatır; özgün prompt değildir. `iterations/` altındaki her dosya bir teknik istek için amaç, verilen bağlam, kaynak istek veya açıkça etiketlenmiş düzenlenmiş anlatım, çıktı değerlendirmesi ve iterasyon notlarını içerir. Açıklama/değerlendirme bölümleri sonradan derlenmiş özetlerdir.

## Görev özetleri

- [Ek UI senaryosu](01-ui-teams-toggle.md)
- [Davranış odaklı API senaryoları](02-api-http-requests.md)
- [JMeter çalışmasında AI desteği](03-load-test-collaboration.md)

## UI kayıtları

- [Başlangıç bağlamı](iterations/ui-01-initial-implementation.md)
- [Kapsam incelemesi](iterations/ui-02-scope-review.md)
- [Görev promptuyla son UI review](iterations/ui-03-final-task-review.md)

## API kayıtları

- [Güncel BDD senaryolarının review kaydı](iterations/api-01-current-bdd-review.md)

## Yük testi kayıtları

- [İlk plan isteği](iterations/load-01-initial-plan.md)
- [Başarısız isteğin incelenmesi](iterations/load-02-failing-request.md)
- [Header mesajının açıklanması](iterations/load-03-header-message.md)
- [HTTP 403 yanıtının değerlendirilmesi](iterations/load-04-http-403.md)
- [Koruma olasılığının değerlendirilmesi](iterations/load-05-protection-hypothesis.md)
- [Bulguların belgelenmesi](iterations/load-06-document-findings.md)
- [Arama sonucu assertion önerisi](iterations/load-07-search-result-assertion.md)
- [Arama assertion ayarlarının kontrolü](iterations/load-08-search-assertion-settings.md)
- [Boş arama sonucu assertion önerisi](iterations/load-09-empty-result-assertion.md)
- [Boş sonuç assertion ayarlarının uygulanması](iterations/load-10-empty-result-settings.md)
- [Güncel JMeter planı ve son review](iterations/load-11-final-task-review.md)

## Kayıtların kapsamı

Üç görevde de AI desteği kullanıldığı için ilgili kayıtlar tutulur. Güncel JMX’i aday manuel yeniden hazırladı; teşhis, HTML incelemesi ve assertion ifadelerindeki AI desteği ayrıca belgelenir. Kayıtlar yukarıdaki üç test görevinin kapsamıyla sınırlıdır.

UI başlangıç kaydı genel uygulama onayının kısa alıntısıdır; ilgili yük testi kaydı aynı geliştirme aşamasının düzenlenmiş anlatımını içerir. Ek ekip kartı senaryosu PDF bağlamından AI tarafından önerildi; gönderilmemiş ayrı bir başlangıç promptu üretilmedi. API kaydı güncel görev metniyle yapılan son incelemeyi içerir. Kaynakta bulunan zaman damgaları korunur, bulunmayanlar tahmin edilmez.

Bunlar ilgili teknik iterasyonların kayıtlarıdır; kişisel sohbetin, araç çağrılarının veya daha sonraki her kod değişikliğinin eksiksiz dökümü değildir. Sonradan hazırlanan profesyonel görev promptları gerçek mesajların yerine kullanılmaz. Güncel sonuçlar [EXECUTION.md](../docs/EXECUTION.md), adayın kişisel değerlendirmesi [AI_USAGE.md](../AI_USAGE.md) içindedir.
