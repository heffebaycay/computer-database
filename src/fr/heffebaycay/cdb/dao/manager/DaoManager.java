package fr.heffebaycay.cdb.dao.manager;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.impl.CompanyDaoMockImpl;
import fr.heffebaycay.cdb.dao.impl.CompanyDaoMySQLImpl;
import fr.heffebaycay.cdb.dao.impl.ComputerDaoMockImpl;
import fr.heffebaycay.cdb.dao.impl.ComputerDaoMySQLImpl;


public enum DaoManager {
	
	INSTANCE;
	
	private ICompanyDao companyDao;
	private IComputerDao computerDao;
	
	private DaoManager() {
		//companyDao = new CompanyDaoMockImpl();
	    companyDao = new CompanyDaoMySQLImpl();
		//computerDao = new ComputerDaoMockImpl();
	    computerDao = new ComputerDaoMySQLImpl();
	}
	
	public ICompanyDao getCompanyDao() {
		return companyDao;
	}
	
	public IComputerDao getComputerDao() {
		return computerDao;
	}
	

}
