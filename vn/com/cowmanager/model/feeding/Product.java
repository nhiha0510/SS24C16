package vn.com.cowmanager.model.feeding;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.util.Tuple;

@DClass(schema = "feeding")
public abstract class Product {

	private static Integer idCounter = 0;

	@DAttr(name = "id", id = true, auto = true, type = Type.String, optional = false, mutable = false, length = 6)
	private String id;

	@DAttr(name = "name", type = Type.String, optional = false, length = 25)
	private String name;

	@DAttr(name = "productType", type = Type.String, length = 15)
	private String productType;

	@DAttr(name = "cost", type = Type.Double, length = 6, optional = false)
	private Double cost;

	@DAttr(name = "mfg", type = Type.String, length = 25)
	private String mfg;

	@DAttr(name = "exp", type = Type.String, length = 25)
	private String exp;

	public Product(String id, String name, String productType, Double cost, String mfg, String exp) {
		this.id = nextId(id);
		this.name = name;
		this.productType = productType;
		this.cost = cost;
		if (validateDate(mfg)) {
			this.mfg = mfg;
		} else {
			throw new NotPossibleException(null,
					"Fomat: dd/MM/yyyy");
		}
		if (validateDate(exp)) {
			this.exp = exp;
		} else {
			throw new NotPossibleException(null,
					"Fomat: dd/MM/yyyy");
		}
	}

	public Product(@AttrRef("name") String name, @AttrRef("productType") String productType,
			@AttrRef("cost") Double cost, @AttrRef("mfg") String mfg, @AttrRef("exp") String exp) {
		this(null, name, productType, cost, mfg, exp);
	}

	public Product(@AttrRef("name") String name, @AttrRef("cost") Double cost, @AttrRef("mfg") String mfg,
			@AttrRef("exp") String exp) {
		this(null, name, null, cost, mfg, exp);
	}

	// automatically generate the next student id
	protected String nextId(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;
			return "P" + idCounter;
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

	private boolean validateDate(String date) throws NotPossibleException {
		// 20/11/1997
		String strNum1 = date.substring(0, 2);
		int num1 = Integer.parseInt(strNum1);
		String strNum2 = date.substring(3, 5);
		int num2 = Integer.parseInt(strNum2);
		String strNum3 = date.substring(6, 10);
		int num3 = Integer.parseInt(strNum3);
		String slash1 = date.substring(2, 3);
		String slash2 = date.substring(5, 6);
		String slash = "/";
		if (slash1.equals(slash) && slash2.equals(slash) && num1 <= 31 && num2 <= 12 && num3 >= 1961) {
			return true;
		}
		return false;
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
		return "Product(" + id + "," + name + "," + productType + "," + cost + ", " + mfg + ", " + exp + ")";
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getMfg() {
		return mfg;
	}

	public void setMfg(String mfg) {
		this.mfg = mfg;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

}
