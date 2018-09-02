public enum Symbol {
	ANSUZ(new Line(1, 0, 1, 12), new Line(1, 0, 5, 4), new Line(1, 4, 5, 8)),
	BERKANO(new Line(1, 0, 1, 12), new Line(1, 0, 5, 3), new Line(5, 3, 1, 6), new Line(1, 6, 5, 9), new Line(5, 9, 1, 12)),
	KENAZ(new Line(6, 0, 0, 6), new Line(0, 6, 6, 12)),
	DAGAZ(new Line(0, 0, 6, 12), new Line(6, 12, 6, 0), new Line(6, 0, 0, 12), new Line(0, 12, 0, 0)),
	EHWAZ(new Line(0, 12, 0, 0), new Line(0, 0, 3, 3), new Line(3, 3, 6, 0), new Line(6, 0, 6, 12)),
	FEHU(new Line(0, 0, 0, 12), new Line(0, 3, 3, 0), new Line(0, 6, 6, 0)),
	GEBO(new Line(0, 0, 6, 12), new Line(0, 12, 6, 0)),
	HAGALAZ(new Line(0, 0, 0, 12), new Line(0, 4, 6, 8), new Line(6, 0, 6, 12)),
	JERA(new Line(3, 0, 0, 4), new Line(0, 4, 3, 8), new Line(3, 4, 6, 8), new Line(6, 8, 3, 12)),
	IGA(new Line(3, 0, 3, 12)),
	LAGUZ(new Line(1, 0, 1, 12), new Line(1, 0, 5, 5)),
	MANNAZ(new Line(0, 0, 0, 12), new Line(0, 0, 6, 6), new Line(0, 6, 6, 0), new Line(6, 0, 6, 12)),
	NAUTHIZ(new Line(3, 0, 3, 12), new Line(0, 3, 6, 9)),
	OTHALA(new Line(0, 12, 6, 4), new Line(6, 4, 3, 0), new Line(3, 0, 0, 4), new Line(0, 4, 6, 12)),
	PERTHRO(new Line(5, 0, 3, 2), new Line(3, 2, 1, 0), new Line(1, 0, 1, 12), new Line(1, 12, 3, 10), new Line(3, 10, 5, 12)),
	INGWAZ(new Line(3, 0, 0, 6), new Line(0, 6, 3, 12), new Line(3, 12, 6, 6), new Line(6, 6, 3, 0)),
	RAIDHO(new Line(1, 12, 1, 0), new Line(1, 0, 4, 3), new Line(4, 3, 1, 6), new Line(1, 6, 5, 10)),
	SOWILO(new Line(5, 0, 1, 4), new Line(1, 4, 5, 8), new Line(5, 8, 1, 12)),
	TIWAZ(new Line(0, 3, 3, 0), new Line(3, 0, 6, 3), new Line(3, 0, 3, 12)),
	WUNJO(new Line(1, 12, 1, 0), new Line(1, 0, 4, 3), new Line(4, 3, 1, 6)),
	THURISAZ(new Line(1, 12, 1, 0), new Line(1, 3, 4, 6), new Line(4, 6, 1, 9)),
	EIHWAZ(new Line(0, 9, 3, 12), new Line(3, 12, 3, 0), new Line(3, 0, 6, 3)),
	ALGIZ(new Line(0, 0, 3, 3), new Line(3, 3, 6, 0), new Line(3, 0, 3, 12)),
	URUZ(new Line(1, 12, 1, 0), new Line(1, 0, 5, 5), new Line(5, 5, 5, 12));

	private Line[] lines;
	
	Symbol(Line... lines) {
		this.lines = lines;
	}
	
	public Line[] getLines() {
		return lines;
	}
	
	public static Symbol random() {
		int r = (int) (Math.random() * values().length);
		return values()[r];
	}
}
