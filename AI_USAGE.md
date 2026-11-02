# AI kullanım değerlendirmesi

## 1. Context, prompt, skill ve agent kavramları

Context, AI'a görevi doğru yapabilmesi için gerekli kodu, bilgileri ve kısıtları uygun zamanda ve formatta sunmaktır; prompt ise istediğimiz işi anlatan komut, soru veya yönlendirmedir. Skill, bir görevin nasıl yapılacağını anlatan yapılandırılmış talimat paketidir; tutarlı iş akışına yardımcı olabilir, ancak tek başına token veya maliyet tasarrufunu garanti etmez. Agent bu bağlam ve talimatlarla araç kullanarak kod yazabilir, API çağrıları yapabilir ve işlemleri yürütebilir; bu çalışmada kod ve API araçları kullanıldı, veritabanı sorgulanmadı, ayrıca asistan değerlendirme PDF'ini okumak için PDF skill'ini kullandı.

## 2. AI çıktısını nasıl doğruladım, neleri reddettim?

Çıktıları farklı AI modelleriyle incelettim ve human-in-the-loop yaklaşımıyla kendim de review ederek senaryoları çalıştırdım. İlk TestNG tabanlı tasarımı tercih etmedim; senaryoların feature dosyalarında açıkça okunabildiği Cucumber ve JUnit Platform yapısına geçilmesini istedim. TestNG de Cucumber ile kullanılabilir; burada okunabilirliği sağlayan BDD senaryo yapısıdır, JUnit ise seçtiğimiz runner altyapısıdır.

## 3. AI'ın zayıf kaldığı nokta ve benim müdahalem

AI’ın ilk çıktısında BDD feature dosyalarının eksik olduğunu fark ederek Cucumber yapısına geçilmesini istedim; ortak HTTP adımlarını ilk aşamada önermiş olsam da daha sonra senaryoların pet işlemlerini anlatan BDD adımlarıyla yazılmasını tercih ettim; step definition’larda regex biçimini ve status kodlarının '400' gibi tırnaklı değerler yerine 400 olarak yazılıp Java tarafında int parametreyle alınmasını istedim; son iki değişiklik hata düzeltmesi değil, okunabilirlik ve tip tercihlerimdi. QA ilanı bulunmadığı için asıl UI senaryosu başarısız olduğunda, açık başka bir ilan üzerinden alternatif senaryo eklenmesini ben önerdim ve ilk alternatifin ana akışı tam kapsamadığını fark ederek genişletilmesini istedim; bu alternatifin geçmesi, asıl QA senaryosunun geçtiği anlamına gelmiyor. Güncellenen senaryoları kendim çalıştırarak doğruladım; kod üzerinde doğrudan manuel değişiklik yapmadım, müdahalem tasarım, review ve test doğrulaması tarafındaydı ve kod değişikliklerini AI uyguladı.

## 4. Hangi durumlarda kodu elle yazmayı tercih ederim?

Yeni bir feature için ilk test senaryosunu ve otomasyon kodunu kendim yazmayı tercih ederim. Sonraki senaryoları bu örneği ve belirlediğim kuralları AI’a bağlam olarak vererek geliştirir, çıktıları kendim kontrol ederim. Böylece AI çıktılarının beklentilerime daha uygun olmasını ve review için harcadığım eforun azalmasını hedeflerim.
