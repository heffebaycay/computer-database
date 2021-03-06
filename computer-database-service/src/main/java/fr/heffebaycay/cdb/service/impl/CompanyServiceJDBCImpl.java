package fr.heffebaycay.cdb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Base implementation of the {@link fr.heffebaycay.cdb.service.ICompanyService} interface.
 *
 */
@Service
public class CompanyServiceJDBCImpl implements ICompanyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceJDBCImpl.class);

  ICompanyDao                 companyDao;

  IComputerDao                computerDao;

  @Autowired
  public CompanyServiceJDBCImpl(IComputerDao computerDao, ICompanyDao companyDao) {
    this.computerDao = computerDao;
    this.companyDao = companyDao;
  }

  public CompanyServiceJDBCImpl(ICompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public List<Company> findAll() {
    LOGGER.debug("Call to findAll()");

    List<Company> companies = companyDao.findAll();

    return companies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Company findById(long id) {
    LOGGER.debug("Call to findById()");

    Company company = companyDao.findById(id);;

    return company;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public long create(Company company) {
    LOGGER.debug("Call to create()");

    return companyDao.create(company);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public SearchWrapper<Company> findAll(CompanyPageRequest request) {
    LOGGER.debug("Call to findAll()");

    SearchWrapper<Company> companies = companyDao.findAll(request);

    return companies;
  }

  @Override
  @Transactional
  public void remove(long id) {
    LOGGER.debug("Call to remove()");

    LOGGER.debug("Are we in a transactionnal context?: {}",
        TransactionSynchronizationManager.isActualTransactionActive());

    // Remove computers linked to company X
    computerDao.removeForCompany(id);

    // Remove company X
    companyDao.remove(id);
  }

  @Override
  @Transactional
  public SearchWrapper<Company> findByName(CompanyPageRequest request) {
    LOGGER.debug("Call to findByName()");

    SearchWrapper<Company> companies = companyDao.findByName(request);

    return companies;
  }

  public void setCompanyDao(ICompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  public void setComputerDao(IComputerDao computerDao) {
    this.computerDao = computerDao;
  }

}
