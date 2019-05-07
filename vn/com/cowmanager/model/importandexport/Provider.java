package vn.com.cowmanager.model.importandexport;

import java.util.ArrayList;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.meta.DAttr.Type;
//import vn.com.cowmanager.model.arrangement.Person;
import vn.com.cowmanager.model.arrangement.Cow;

@DClass(schema = "bussiness")
public class Provider {

	private static int idCounter = 0;

	@DAttr(name = "manufacturer", type = Type.String, length = 25)
	private String manufacturer;

	@DAttr(name = "lstSheep", type = Type.Collection, filter = @Select(clazz = Cow.class), optional = true, serialisable = false)
	@DAssoc(ascName = "provider-has-sheep", ascType = AssocType.One2Many, endType = AssocEndType.One, role = "provider", associate = @Associate(cardMax = 25, cardMin = 1, type = Cow.class))
	private List<Cow> lstSheep;
	private static int sheepCount;

	public Provider(String id, String name, String address, String phoneNum, String manufacturer) {
				this.manufacturer = manufacturer;

		lstSheep = new ArrayList<>();
		sheepCount = 0;
	}

	public Provider(String name, String address, String phoneNum, String manufacturer) {
		this(null, name, address, phoneNum, manufacturer);
	}

	public Provider(String name, String address, String phoneNum) {
		this(null, name, address, phoneNum);
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	// only need to do this for reflexive association:
	// @MemberRef(name="students")
	public boolean addSheep(Cow s) {
		if (!this.lstSheep.contains(s)) {
			lstSheep.add(s);
		}

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewSheep(Cow s) {
		lstSheep.add(s);
		sheepCount++;

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	public boolean addSheep(List<Cow> lstSheep) {
		for (Cow s : lstSheep) {
			if (!this.lstSheep.contains(s)) {
				this.lstSheep.add(s);
			}
		}

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewSheep(List<Cow> lstSheep) {
		this.lstSheep.addAll(lstSheep);
		sheepCount += lstSheep.size();

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkRemover)
	// only need to do this for reflexive association:
	// @MemberRef(name="students")
	public boolean removeSheep(Cow s) {
		boolean removed = lstSheep.remove(s);

		if (removed) {
			sheepCount--;
		}

		// no other attributes changed
		return false;
	}

	public List<Cow> getLstSheep() {
		return lstSheep;
	}

	public void setLstSheep(List<Cow> lstSheep) {
		this.lstSheep = lstSheep;
		sheepCount = lstSheep.size();
	}

	public static int getSheepCount() {
		return sheepCount;
	}

	public static void setSheepCount(int sheepCount) {
		Provider.sheepCount = sheepCount;
	}

	// automatically generate the next student id
	protected String nextId(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;
			return "PR" + idCounter;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

}
