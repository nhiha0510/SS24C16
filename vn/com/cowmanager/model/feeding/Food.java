package vn.com.cowmanager.model.feeding;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;

@DClass(schema = "feeding")
public class Food extends Product {
	private static int idCounter = 0;

	@DAttr(name = "userManual", type = Type.String, length = 50, optional = false)
	private String userManual;

	public Food(String id, String name, String productType, Double cost, String mfg, String exp, String userManual) {
		super(id, name, productType, cost, mfg, exp);
		this.userManual = userManual;
	}

	public Food(String name, String productType, Double cost, String mfg, String exp, String userManual) {
		this(null, name, productType, cost, mfg, exp, userManual);
	}

	@Override
	protected String nextId(String id) throws ConstraintViolationException {
		if (id == null) {
			idCounter++;
			return "F" + idCounter;
		} else {
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

	public String getUserManual() {
		return userManual;
	}

	public void setUserManual(String userManual) {
		this.userManual = userManual;
	}
}
