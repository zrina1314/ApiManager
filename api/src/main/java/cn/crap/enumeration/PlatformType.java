package cn.crap.enumeration;

public enum PlatformType {
	EGMAS("EGMAS"), ANDROID("ANDROID"), IOS("IOS"), TEST("TEST");
	private final String name;

	private PlatformType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
