package vn.com.cowmanager.model.importandexport;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DAttr.Type;
//import vn.com.cowmanager.model.arrangement.Person;

@DClass(schema = "business")
public class Customer {

	private static int idCounter = 0;

	@DAttr(name = "organization", type = Type.String, length = 25)
	private String organization;

	public Customer(String id, String name, String address, String phoneNum, String organization) {
				this.organization = organization;
	}

	public Customer(String name, String address, String phoneNum, String organization) {
		this(null, name, address, phoneNum, organization);
	}

	public Customer(String name, String address, String phoneNum) {
		this(null, name, address, phoneNum, null);
	}

	protected String nextId(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;
			return "CS" + idCounter;
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
