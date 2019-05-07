package vn.com.cowmanager.model.treatment;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
//import vn.com.cowmanager.model.feeding.Product;
import domainapp.basics.model.meta.DClass;

@DClass(schema = "treatment")
public class Drug {
	private static int idCounter = 0;

	@DAttr(name = "preservationRule", type = Type.String, length = 50, optional = false)
	private String preservationRule;

	public Drug(String id, String name, String productType, Double cost, String mfg, String exp,
			String preservationRule) {
		this.preservationRule = preservationRule;
	}

	public Drug(String name, String productType, Double cost, String mfg, String exp, String preservationRule) {
		this(null, name, productType, cost, mfg, exp, preservationRule);
	}

	
	/*protected String nextId(String id) throws ConstraintViolationException {
		if (id == null) {
			idCounter++;
			return "D" + idCounter;
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

	public String getPreservationRule() {
		return preservationRule;
	}

	public void setPreservationRule(String preservationRule) {
		this.preservationRule = preservationRule;
	} */

}
