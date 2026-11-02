# Ek UI senaryosunun görev promptuyla son incelemesi

İnceleme tarihi: 2 Kasım 2026. Bu dosya, mevcut uygulamaya yönelik yeni review iterasyonudur; geçmişteki oluşturma isteğinin yerine geçmez. Değerlendirme asistanın bulgularıdır, adayın henüz vermediği onay olarak sunulmaz.

## 1. Amaç

Ek ekip kartı senaryosunu adayın gönderdiği görev promptunun her beklentisiyle karşılaştırmak.

## 2. Verilen bağlam

Aday UI görev promptunu bu görüşmede yeniden gönderdi, üç görev dosyasının da değerlendirmeye alınmasını istedi ve “tamamdır ilerleyelim” mesajıyla incelemeyi başlattı. Mevcut CareersPage, CareersStepdefs ve careers.feature incelendi. Kaynak görev metni: [01-ui-teams-toggle.md](../01-ui-teams-toggle.md).

## 3. Bu review’ü başlatan gerçek istek

Üç görev dosyasının değerlendirmeye alınması konuşmada netleştirildikten sonra adayın gönderdiği mesaj:

~~~~text
tamamdır ilerleyelim
~~~~

### Bu iterasyonda kullanılan görev metni

Aşağıdaki metin kaynak dosyadan aynen alınmış ve bu incelemeye girdi olarak kullanılmıştır; ilk uygulamanın geçmiş promptu olduğu iddia edilmez.

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

## 4. Çıktı değerlendirmesi

| Beklenti | İncelenen karşılık |
| --- | --- |
| Başlangıç kartlarını kaydetme | open() → initiallyVisibleTeams |
| Genişletince daha fazla kart | assertExpandedTeamCards() → başlangıçla sayı karşılaştırması |
| Benzersizlik ve kart içeriği | Set karşılaştırması; assertTeamCard() ile başlık, ilan sayısı, HTTPS Lever işveren bağlantısı |
| İlanı olan ekipte filtre | Sıfır olmayan sayıda hasTeamFilter() |
| İlanı olmayan ekibi dışlamama | Sıfır ilan kabul edilir; pozitif sayı zorunluluğu yok |
| Kapatınca ilk duruma dönüş | assertOnlyInitialTeamsVisible() |
| Yeniden açınca aynı sıra | assertSameTeamsVisibleAgain() → List.equals |
| Explicit wait, Page Object ve regex | WebDriverWait ve mevcut Page Object/regex adımları |
| Ad ve tagler | Verify that, @teamsToggle, @aiGenerated, @positive; @wip yok |

Mevcut senaryo kabul edildi; aynı kapsamı tekrar eden yeni senaryo veya sırf inceleme için kod değişikliği eklenmedi. İlanı olmayan kartları başarısız saymak ve gösterim başlığını departman kimliğiyle zorunlu eşitlemek benimsenmedi. Son canlı supplemental seçimde iki senaryo da Chrome SessionNotCreatedException nedeniyle sayfa adımlarına başlamadan başarısız oldu. Bu sonuç kod beklentilerinin statik karşılaştırmasından ayrıdır; başarılı UI koşumu olarak sunulmaz.

## 5. İterasyon notları

Bu inceleme mevcut uygulamanın yeni değerlendirmesidir; ilk oluşturma öncesi geçmiş prompt gibi sunulmaz. Tarayıcı çalışabilen ortamda son UI koşumunun tekrar doğrulanması gerekir.
