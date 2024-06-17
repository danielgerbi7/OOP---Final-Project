package daniel_strasser_daniel_jerbi;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Shift {

	private HairDresser[] hairdressers;
	private Treatment[] treatments;
	private int numOfHairdressers;
	private int numOfTreatments;
	private LocalDateTime date, startTime, endTime;
	private int coffeeAndPastryPrice = 10;

	public Shift(Hairdresser[] hairdressers, Treatment[] treatments, LocalDateTime date, LocalDateTime startTime,
			LocalDateTime endTime) throws InvalidRangeException {

		// the constructor will roll the InvalidRangeException to main via
		// HairDressingSalon,
		// main will output the exception message

		setStartAndEndTime(startTime, endTime); // throws InvalidRangeException
		this.hairdressers = hairdressers;
		this.treatments = treatments;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int calcProfit() {
		int profit = 0;
		for (int i = 0; i < treatments.length; i++) {
			if (treatments[i].getHairdresser instanceof VIPable) {
				profit -= coffeeAndPastryPrice;
			}
			if (treatments[i].getClient instanceof VIPable) {
				profit -= coffeeAndPastryPrice;
			}
			profit += treatments[i].getPayment;
		}
		return profit;
	}

	public void sortTreatments() {
		for (int i = 0; i < treatments.length; i++) {
			for (int j = 1; j < treatments.length - 1; j++) {
				if (treatments[j - 1].getStartTime.compareTo(treatments[j].getStartTime) > 0) {
					swapTreatments(treatments[j - 1], treatments[j]);
				}
			}
		}
	}

	public boolean addTreatment(Hairdresser hairdresser, Client client, LocalDateTime startTime, eType type,
			int payment) throws InvalidTimeException, HairdresserAbsentException, HairdresserBusyException {
		Treatment t = new Treatment(hairdresser, client, startTime, type, payment);
		checkValidTime(t);
		checkHairdresserIsAvailable(hairdresser, startTime);
		if (numOfTreatments == treatments.length) {
			treatments = Arrays.copyOf(treatments, treatments.length + 1);
		}
		treatments[numOfTreatments++] = t;
		sortTreatments();
	}

	public void swapTreatments(Treatment t1, Treatment t2) {
		Treatment tmp;
		tmp = t1;
		t1 = t2;
		t2 = tmp;
	}

	
	public HairDresser[] getHairdressers() {
		return hairdressers;
	}

	public void setHairdressers(HairDresser[] hairdressers) {
		this.hairdressers = hairdressers;
	}

	public Treatment[] getTreatments() {
		return treatments;
	}

	public void setTreatments(Treatment[] treatments) {
		this.treatments = treatments;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartAndEndTime(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime date) throws InvalidRangeException {

		if (startTime.compareTo(endTime) > 0) {
			throw new InvalidDateTime();
		} else {
			this.startTime = startTime;
			this.endTime = endTime;
			this.date = date;
		}
	}

	public void checkValidTime(Treatment t) {
		if (t.startTime < this.startTime || t.startTime > this.endTime) {
			throw new InvalidTimeException();
		}
	}

	public void checkHairdresserIsAvailable(Hairdresser h, startTime st) {

		boolean presentFlag = false;
		for (int i = 0; i < hairdressers.length; i++) {
			if (hairdressers[i] == h) {
				presentFlag = true;
			}
		}
		if (presentFlag == false) {
			throw new HairdresserIsAbsent();
		}
		for (int j = 0; j < treatments.length; j++) {
			if (treatments[j].getHairdresser == h && treatments[j].getStartTime == st) {
				throw new HairdresserIsBusy();
				// other conditions could be considered, for example a minimum length of
				// treatment, or a length of treatment according to its type
			}
		}
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Shift)) {
			return false;
		}
		Shift s = (Shift) other;
		return this.hairdressers.equals(s.hairdressers) && this.treatments.equals(s.treatments)
				&& this.date.compareTo(s.date) == 0 && this.startTime.compareTo(s.startTime) == 0
				&& this.endTime.compareTo(s.endTime) == 0;
		// the Hairdresser.equals and Treatment.equals methods will similarly compare
		// their respective fields, or some of them, according to needs.
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();

		res.append("Date: " + date.toString() + "\n");
		res.append("Start time: " + startTime.toString() + "\n");
		res.append("End time: " + endTime.toString() + "\n");

		for (int i = 0; i < hairdressers.length; i++) {
			res.append("Hairdresser #" + (i + 1) + ": " + hairdressers[i].toString() + "\n");
		}
		res.append("\n");
		for (int j = 0; j < treatments.length; j++) {
			res.append("Treatment #" + (j + 1) + ": " + treatments[j].toString() + "\n");
		}
		// Even as there is the possibiltiy of duplicated information,
		// e.g between Hairdresser.toString and Treatment.toString(),
		// which contains Hairdresser's information,
		// and between date.toString and Treatment.toString(),
		// this duplication can be used to monitor activity in the salon.
		// Alternatively these redundancies can be removed with different toString
		// methods.
	}

}
