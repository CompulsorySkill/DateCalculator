package src;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class MyDate
{
	private final LocalDate date;
	private static final DateTimeFormatter formatIn = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	private static final DateTimeFormatter formatOut = DateTimeFormatter.ofPattern("yyyy/MM/dd E");

	public MyDate(final String dateStr) {
		this.date = LocalDate.parse(dateStr, MyDate.formatIn);
	}

	public String toString() {
		return date.format(MyDate.formatOut);
	}

	public String add(final long days) {
		final LocalDate res = date.plusDays(days);
		return res.format(MyDate.formatOut);
	}

	public String add(final String dateStr) {
		final LocalDate other = LocalDate.parse(dateStr, MyDate.formatIn);
		LocalDate res = date.plusDays(other.getDayOfYear());
		res = res.plusYears(other.getYear());
		return res.format(MyDate.formatOut);
	}

	public String minus(final long days) {
		final LocalDate res = date.minusDays(days);
		return res.format(MyDate.formatOut);
	}

	public String minus(final String dateStr) {
		final LocalDate other = LocalDate.parse(dateStr, MyDate.formatIn);
		long res = this.date.toEpochDay() - other.toEpochDay();
		return String.valueOf(res);
	}
}
