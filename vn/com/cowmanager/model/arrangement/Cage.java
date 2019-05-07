package vn.com.cowmanager.model.arrangement;

import java.util.ArrayList;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;

@DClass(schema = "arrangement")
public class Cage {

	private static int idCounter = 0;
	@DAttr(name = "id", type = Type.String, id = true, auto = true, mutable = false, optional = false, length = 6)
	private String id;

	@DAttr(name = "name", type = Type.String, optional = false, length = 25)
	private String name;

	@DAttr(name = "cardMax", type = Type.Integer, optional = false, length = 6)
	private Integer cardMax;

	@DAttr(name = "lstCow", type = Type.Collection, filter = @Select(clazz = Cow.class), optional = false, serialisable = false)
	@DAssoc(ascName = "cage-has-cow", ascType = AssocType.One2Many, endType = AssocEndType.One, role = "cage", associate = @Associate(cardMax = 25, cardMin = 1, type = Cow.class))
	private List<Cow> lstCow;

	private static int cowCount;

	@DAttr(name = "location", type = Type.String, length = 50)
	private String location;

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Cage(String id, String name, Integer cardMax, String location) {
		this.id = nextId(id);
		this.name = name;
		this.location = location;
		this.cardMax = cardMax;
		cowCount = 0;
		lstCow = new ArrayList<>();

	}

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Cage(@AttrRef("name") String name, @AttrRef("cardMax") Integer cardMax,
			@AttrRef("location") String location) {
		this(null, name, cardMax, location);
	}

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public Cage(@AttrRef("name") String name, @AttrRef("cardMax") Integer cardMax) {
		this(null, name, cardMax, null);
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	// only need to do this for reflexive association:
	// @MemberRef(name="students")
	public boolean addCow(Cow s) throws NotPossibleException {
		if (!this.lstCow.contains(s)) {
			lstCow.add(s);
		}
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewCow(Cow s) {
		lstCow.add(s);
		cowCount++;
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	public boolean addCow(List<Cow> lstCow) {
		for (Cow s : lstCow) {
			if (!this.lstCow.contains(s)) {
				this.lstCow.add(s);
			}
		}

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewCow(List<Cow> lstCow) {
		this.lstCow.addAll(lstCow);
		cowCount += lstCow.size();

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkRemover)
	// only need to do this for reflexive association:
	// @MemberRef(name="students")
	public boolean removeCow(Cow s) {
		boolean removed = lstCow.remove(s);

		if (removed) {
			cowCount--;
		}

		// no other attributes changed
		return false;
	}

	public List<Cow> getLstCow() {
		return lstCow;
	}

	public void setLstCow(List<Cow> lstCow) {
		this.lstCow = lstCow;
		cowCount = lstCow.size();
	}

	public int getCowCount() {
		return cowCount;
	}

	public static void setCowCount(int cowCount) {
		Cage.cowCount = cowCount;
	}

	// automatically generate the next student id
	private String nextId(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;
			return "C" + idCounter;
		} else {
			// update id
			int num;
			try {
				num = Integer.parseInt(id.substring(1));
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
				int maxIdNum = Integer.parseInt(maxId.substring(1));

				if (maxIdNum > idCounter) // extra check
					idCounter = maxIdNum;

			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { maxId });
			}
		}
	}

	public String toString() {
		String result = "Cage(" + id + "," + name + "," + location + ", " + "lstCow: ";
		for (Cow s : lstCow) {
			result += s + "\n";
		}
		return result + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCardMax() {
		return cardMax;
	}

	public void setCardMax(Integer cardMax) {
		this.cardMax = cardMax;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
