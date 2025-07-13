package com.finalYearProject.product.constant;

import java.util.Arrays;
import java.util.Optional;

public enum MaterialType {
    // Doğal Lifler (Bitkisel Kökenli)
    COTTON("Pamuk", "Yumuşak, nefes alabilen doğal elyaf. Organik versiyonu daha sürdürülebilirdir."),
    ORGANIC_COTTON("Organik Pamuk", "Pestisit ve kimyasal gübre kullanılmadan yetiştirilen pamuk. Geleneksel pamuğa göre daha az su ve enerji tüketir."),
    LINEN("Keten", "Dayanıklı, nefes alabilen ve az su gerektiren doğal elyaf."),
    HEMP("Kenevir", "Çok dayanıklı, az su ve pestisit gerektiren, hızlı büyüyen doğal elyaf."),
    BAMBOO("Bambu", "Hızlı büyüyen, biyobozunur ve genellikle yumuşak bir elyaf. Ancak işleme süreci kimyasal yoğun olabilir."),
    JUTE("Jüt", "Dayanıklı, biyobozunur ve düşük karbon ayak izine sahip doğal elyaf."),
    RAMIE("Rami", "Keten benzeri, parlak ve dayanıklı doğal elyaf."),
    COCONUT_FIBER("Hindistan Cevizi Lifi", "Hindistan cevizinden elde edilen, dayanıklı ve doğal elyaf."),

    // Doğal Lifler (Hayvansal Kökenli)
    WOOL("Yün", "Koyun yününden elde edilen doğal elyaf. Sürdürülebilirlik, hayvan refahına bağlıdır."),
    ORGANIC_WOOL("Organik Yün", "Organik standartlara uygun yetiştirilen koyunlardan elde edilen yün."),
    CASHMERE("Kaşmir", "Kaşmir keçisinden elde edilen lüks ve yumuşak yün. Üretimi çevresel etki yaratabilir."),
    SILK("İpek", "İpek böceğinden elde edilen doğal protein lifi. Üretimi su ve enerji yoğun olabilir."),
    ANGORA("Angora", "Angora tavşanından elde edilen yumuşak yün."),
    ALPACA("Alpaka", "Alpaka hayvanından elde edilen hipoalerjenik ve dayanıklı lif."),
    LEATHER("Deri", "Hayvan derisinden elde edilen dayanıklı malzeme. Çevresel ve etik kaygılar taşıyabilir."),
    SUEDE("Süet", "Deri altından elde edilen yumuşak yüzeyli malzeme."),
    FUR("Kürk", "Hayvan kürkü. Yüksek etik ve çevresel kaygılar taşır."),

    // Yarı Sentetik / Yeniden Üretilmiş Lifler (Rejeneratif Lifler)
    VISCOSE("Viskon (Rayon)", "Ağaç hamurundan elde edilen, ipek benzeri bir elyaf. Üretim süreci kimyasal yoğun olabilir."),
    MODAL("Modal", "Kayın ağacından elde edilen, viskondan daha dayanıklı ve yumuşak bir elyaf."),
    LYOCELL("Lyocell (Tencel)", "Okaliptüs ağacından elde edilen, kapalı döngü üretim süreci sayesinde çevre dostu bir elyaf."),
    CUPRO("Cupro", "Pamuk linterinden (pamuk tohumu etrafındaki kısa lifler) elde edilen, ipek benzeri elyaf."),

    // Sentetik Lifler
    POLYESTER("Polyester", "Petrol bazlı sentetik elyaf. Dayanıklı ve kırışmaya dirençli, ancak biyobozunur değildir."),
    RECYCLED_POLYESTER("Geri Dönüştürülmüş Polyester", "Plastik şişelerden veya atık polyesterden üretilen elyaf. Yeni polyester üretimine göre daha az enerji ve kaynak tüketir."),
    NYLON("Naylon (Polyamid)", "Petrol bazlı sentetik elyaf. Dayanıklı ve esnek."),
    RECYCLED_NYLON("Geri Dönüştürülmüş Naylon", "Atık naylondan (balık ağları vb.) üretilen elyaf."),
    ACRYLIC("Akrilik", "Yün benzeri sentetik elyaf. Biyobozunur değildir."),
    ELASTANE("Elastan (Spandex/Lycra)", "Esneklik sağlayan sentetik elyaf. Genellikle diğer liflerle karıştırılır."),
    POLYPROPYLENE("Polipropilen", "Hafif ve suya dayanıklı sentetik elyaf."),
    POLYETHYLENE("Polietilen", "Plastik bazlı sentetik elyaf, bazen giyimde kullanılır."),

    // Özel / Yenilikçi Malzemeler
    VEGAN_LEATHER_PU("Vegan Deri (PU)", "Poliüretan bazlı sentetik deri alternatifi."),
    VEGAN_LEATHER_MICROFIBER("Vegan Deri (Mikrofiber)", "Mikrofiberden yapılmış sentetik deri alternatifi."),
    MUSHROOM_LEATHER("Mantar Deri", "Mantar miselyumundan üretilen biyobozunur deri alternatifi."),
    PINEAPPLE_LEATHER("Ananas Lifi (Piñatex)", "Ananas yapraklarından elde edilen sürdürülebilir deri alternatifi."),
    APPLE_LEATHER("Elma Deri", "Elma atıklarından üretilen sürdürülebilir deri alternatifi."),
    COFFEE_FABRIC("Kahve Kumaşı", "Kullanılmış kahve telvesinden üretilen kumaş."),
    RECYCLED_COTTON("Geri Dönüştürülmüş Pamuk", "Tekstil atıklarından veya kullanılmış pamuktan üretilen elyaf."),
    BIO_PLASTIC("Biyo-Plastik", "Yenilenebilir kaynaklardan elde edilen plastikler, bazen giyimde kullanılır."),

    // Diğer / Karışım Malzemeler
    BLEND("Karışım", "Birden fazla elyafın birleşimi."),
    METAL("Metal", "Takı ve aksesuar yapımında kullanılan metaller (Geri dönüştürülmüş metal tercih edilir)."),
    STONE("Taş", "Takı ve aksesuar yapımında kullanılan doğal veya sentetik taşlar."),
    GLASS("Cam", "Takı ve aksesuar yapımında kullanılan cam."),
    CERAMIC("Seramik", "Takı ve aksesuar yapımında kullanılan seramik."),
    WOOD("Ahşap", "Takı, ayakkabı topuğu veya aksesuar yapımında kullanılan ahşap."),
    RUBBER("Kauçuk", "Ayakkabı tabanı veya aksesuar yapımında kullanılan doğal veya sentetik kauçuk."),
    UNKNOWN("Bilinmiyor", "Malzeme türü belirtilmemiş veya bilinmiyor.");

    private final String displayName;
    private final String description;

    MaterialType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }


    public static Optional<MaterialType> fromDisplayName(String displayName) {
        return Arrays.stream(MaterialType.values())
                .filter(type -> type.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst();
    }
}
