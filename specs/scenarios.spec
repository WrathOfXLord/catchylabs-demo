# scenarios

//scenario1
//bug
HesapAdiDuzenlemeYalnizcaNumerikDegerIcermemeTesti
------------
@tags:verifyAccountNameIsAlphaNumeric

* Kullanici giris sayfasina gidilir
* Kullanici bilgileri girilir
* Login butonu tiklanir
* Open Money Transfer butonu tiklanir
* Edit Account butonu tiklanir
* Account name alanina "0147" degeri girilir
* Update Account butonu tiklanir
* Hesap adinin yalnizca sayi icermedigi dogrulanir

//bug
//scenario2
EklenenParaMiktariDogrulamaTesti
------------
@tags:verifyAddedAmountIsCorrect

* Kullanici giris sayfasina gidilir
* Kullanici bilgileri girilir
* Login butonu tiklanir
* Open Money Transfer butonu tiklanir
* Hesaptaki ilk para degerini kaydet
* Para ekle butonu tiklanir
* Ornek kart bilgileri girilir
* "10" tutarinda deger amount alanina girilir
* Ekle butonu tiklanir
* Hesaptaki ikinci para degerini kaydet
* Hesaba eklenen para miktarinin "10" oldugu dogrulanir

//scenario3
YapilanIslemSonrasiToplamIslemSayisiDogrulamaTesti
------------
@tags:verifyTransactionCountAfterAddingFunds

* Kullanici giris sayfasina gidilir
* Kullanici bilgileri girilir
* Login butonu tiklanir
* Open Money Transfer butonu tiklanir
* Ilk islem sayisi kaydedilir
* Para ekle butonu tiklanir
* Ornek kart bilgileri girilir
* "100" tutarinda deger amount alanina girilir
* Ekle butonu tiklanir
* Geri butonu tiklanir
* Open Money Transfer butonu tiklanir
* Ikinci islem sayisi kaydedilir
* Islem sonrasi islem sayisinin bir arttigi dogrulanir


//bug
//scenario4
GonderilenParaTutariDogrulamaTesti
------------
@tags:verifySentAmountIsCorrect

* Kullanici giris sayfasina gidilir
* Kullanici bilgileri girilir
* Login butonu tiklanir
* Open Money Transfer butonu tiklanir
* Para transferi butonu tiklanir
* Alici hesap "Testinium-1" olarak secilir
* Transfer tutari "149" olarak girilir
* Gonder butonu tiklanir
* Son islemin "149" tutarini icerdigi dogrulanir