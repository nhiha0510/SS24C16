package vn.com.cowmanager.model.feeding;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import vn.com.cowmanager.model.arrangement.Cage;

@DClass(schema = "feeding")
public class FeedingManager {

	private static int idCounter = 0;

	@DAttr(name = "id", type = Type.String, id = true, auto = true, optional = false, mutable = false, length = 10)
	private String id;
	@DAttr(name = "storage", type = Type.Domain, optional = false, length = 6)
	@DAssoc(ascName = "feedingManager-has-storage", ascType = AssocType.One2Many, endType = AssocEndType.Many, role = "FeedingManager", associate = @Associate(cardMax = 25, cardMin = 1, type = Storage.class))
	private Storage storage;

	@DAttr(name = "totalQuantityInStorage", type = Type.Integer, length = 6, mutable = false)
	private Integer totalQuantityInStorage;

	@DAttr(name = "quantityForFeeding", type = Type.Integer, optional = false, length = 6)
	private Integer quantityForFeeding;

	@DAttr(name = "cage", type = Type.Domain, optional = false, length = 6)
	@DAssoc(ascName = "feedingManager-has-cage", ascType = AssocType.One2Many, endType = AssocEndType.Many, role = "FeedingManager", associate = @Associate(cardMax = 25, cardMin = 1, type = Cage.class))
	private Cage cage;

	@DAttr(name = "eatingTime", type = Type.String, optional = false, length = 20)
	private String eatingTime;

	@DAttr(name = "phase", type = Type.Integer, mutable = false, length = 6)
	private Integer phase;

	public FeedingManager(String id,
			Cage cage, String eatingTime, Integer phase) {
		this.id = nextId(id);
		this.storage = storage;
		this.totalQuantityInStorage = storage.getQuantity();
		this.quantityForFeeding = quantityForFeeding;
		this.cage = cage;
		if (validateTimeFormat(eatingTime)) {
			this.eatingTime = eatingTime;
		} else {
			throw new NotPossibleException(null,
					"Ä�á»‹nh dáº¡ng ngÃ y khÃ´ng há»£p lá»‡, thá»�i Ä‘Æ°á»£c Ä‘á»‹nh dáº¡ng Ä‘Ãºng theo: HHhmm \n vÃ­ dá»¥ nhÆ° 20h15");
		}

		this.phase = getPhase(eatingTime);
		Integer rest = storage.getQuantity() - quantityForFeeding;
		storage.setQuantity(rest);
	}

	public FeedingManager(@AttrRef("storage") Storage storage,
			@AttrRef("totalQuantityInStorage") Integer totalQuantityInStorage,
			@AttrRef("quantityForFeeding") Integer quantityForFeeding, @AttrRef("cage") Cage cage,
			@AttrRef("eatingTime") String eatingTime, @AttrRef("phase") Integer phase) {
		this(null, storage, totalQuantityInStorage, quantityForFeeding, cage, eatingTime, phase);
	}

	// automatically generate the next student id
	private String nextId(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;
			return "FM" + idCounter;
		} else {
			// update id
			int num;
			try {
				num = Integer.parseInt(id.substring(2));
			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { id });
			}

			if (num > idCounter) {
				idCounter = num;
			}

			return id;
		}
	}

	/**
	 * @requires minVal != null /\ maxVal != null
	 * @effects update the auto-generated value of attribute <tt>attrib</tt>,
	 *          specified for <tt>derivingValue</tt>, using
	 *          <tt>minVal, maxVal</tt>
	 */
	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
			throws ConstraintViolationException {

		if (minVal != null && maxVal != null) {
			// TODO: update this for the correct attribute if there are more
			// than one auto attributes of this class

			String maxId = (String) maxVal;

			try {
				int maxIdNum = Integer.parseInt(maxId.substring(2));

				if (maxIdNum > idCounter) // extra check
					idCounter = maxIdNum;

			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { maxId });
			}
		}
	}

	private boolean validateTimeFormat(String eatingTime) {
		String strNum1 = eatingTime.substring(0, 2);
		int num1 = Integer.parseInt(strNum1);
		String strNum2 = eatingTime.substring(3, 5);
		int num2 = Integer.parseInt(strNum2);

		if (num1 <= 24 && num2 <= 60) {
			return true;
		}
		return false;
	}

	public Integer getPhase(String time) {
		String timeCut = time.substring(0, 2);
		Integer iTime = Integer.parseInt(timeCut);
		if (iTime < 9) {
			return 1;
		} else if (iTime < 15) {
			return 2;
		}
		return 3;
	}

	public String toString() {
		return "FeedingManager(" + id + "," + quantityForFeeding + "," + cage.getName() + ")";
	}

	public String getId() {
		return id;
	}

	public Integer getQuantityForFeeding() {
		return quantityForFeeding;
	}

	public void setQuantityForFeeding(Integer quantityForFeeding) {
		this.quantityForFeeding = quantityForFeeding;
	}

	public Cage getCage() {
		return cage;
	}

	public void setCage(Cage cage) {
		this.cage = cage;
	}

	public String getEatingTime() {
		return eatingTime;
	}

	public void setEatingTime(String eatingTime) {
		if (validateTimeFormat(eatingTime)) {
			this.eatingTime = eatingTime;
		} else {
			throw new NotPossibleException(null,
					"Ä�á»‹nh dáº¡ng ngÃ y khÃ´ng há»£p lá»‡, thá»�i Ä‘Æ°á»£c Ä‘á»‹nh dáº¡ng Ä‘Ãºng theo: HHhmm \n vÃ­ dá»¥ nhÆ° 20h15");
		}
	}

	public Integer getPhase() {
		return phase;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		if (storage != null && !storage.equals(this.storage)) {
			this.storage = storage;
			totalQuantityInStorage = storage.getQuantity();
			Integer rest = storage.getQuantity() - quantityForFeeding;
			storage.setQuantity(rest);
		}

	}

	public Integer getTotalQuantityInStorage() {
		return totalQuantityInStorage;
	}

}