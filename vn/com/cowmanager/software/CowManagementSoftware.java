
package vn.com.cowmanager.software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;

import vn.com.cowmanager.model.arrangement.Cage;
import vn.com.cowmanager.model.arrangement.CageManager;
import vn.com.cowmanager.model.arrangement.Facility;
import vn.com.cowmanager.model.arrangement.Cow;
import vn.com.cowmanager.model.importandexport.Customer;
import vn.com.cowmanager.model.importandexport.Provider;
import vn.com.cowmanager.model.treatment.Doctor;
import vn.com.cowmanager.model.treatment.Drug;
public class CowManagementSoftware extends DomainAppToolSoftware {

	// the domain model of software
	private static final Class[] model = { Cage.class, Cow.class, CageManager.class, Facility.class, Customer.class, Provider.class, Doctor.class, Drug.class };

	@Override
	protected Class[] getModel() {
		return model;
	}

	public static void main(String[] args) throws NotPossibleException {
		new CowManagementSoftware().exec(args);
	}

}
