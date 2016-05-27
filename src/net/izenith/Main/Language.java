package net.izenith.Main;

public enum Language {

	AFRIKAANS("af"),
	ALBANIAN("sq"),
	ARABIC("ar"),
	ARMENIAN("hy"),
	BASQUE("eu"),
	BELARUSIAN("be"),
	BULGARIAN("bg"),
	CATALAN("ca"),
	CHINESE("zh"),
	CROATION("hr"),
	CZECH("cs"),
	DANISH("da"),
	DUTCH("nl"),
	ESTONIAN("et"),
	ENGLISH("en"),
	FINNISH("fi"),
	FRENCH("fr"),
	GALICIAN("gl"),
	GEORGIAN("ka"),
	GERMAN("de"),
	GREEK("el"),
	HEBREW("he"),
	HUNGARIAN("hu"),
	ICELANDIC("is"),
	INDONESIAN("id"),
	ITALIAN("it"),
	JAPANESE("ja"),
	KAZAKH("kk"),
	KOREAN("ko"),
	LATVIAN("lv"),
	LITHUANIAN("lt"),
	MACEDONIAN("mk"),
	MALAY("ms"),
	MONGOLIAN("mn"),
	NORWEGIAN("nb"),
	POLISH("pl"),
	PORTUGUESE("pt"),
	ROMANIAN("ro"),
	RUSSIAN("ru"),
	SLOVAK("sk"),
	SLOVENIAN("sl"),
	SPANISH("es"),
	SWAHILI("sw"),
	SWEDISH("sv"),
	TAGALOG("tl"),
	TATAR("tt"),
	THAI("th"),
	TURKISH("tr"),
	UKRAINIAN("uk"),
	UZBEK("uz"),
	VIETNAMESE("vi"),
	WELSH("cy");
	
	private String code;
	
	private Language(String code){
		this.code = code;
	}
	
	public String code(){
		return code;
	}
	
	public static Language getByCode(String code){
		for(Language l : values()){
			if(l.code().equalsIgnoreCase(code))
				return l;
		}
		return null;
	}
	
	public static Language getByName(String name){
		for(Language l : values()){
			if(l.name().equalsIgnoreCase(name)){
				return l;
			}
		}
		return null;
	}
	
}
