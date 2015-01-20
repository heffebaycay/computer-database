package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.impl.util.IMySQLUtils;
import fr.heffebaycay.cdb.dao.impl.util.MySQLProdUtils;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class CompanyDaoMySQLImpl implements ICompanyDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoMySQLImpl.class);
  
  private IMySQLUtils sqlUtils;
  
  public CompanyDaoMySQLImpl() {
    sqlUtils = new MySQLProdUtils(); 
  }
  
  public CompanyDaoMySQLImpl(IMySQLUtils sqlUtils) {
    this.sqlUtils = sqlUtils;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll() {

    Connection conn = sqlUtils.getConnection();

    final String query = "SELECT id, name FROM company";
    ResultSet results;
    List<Company> companies = new ArrayList<Company>();

    try {
      Statement stmt = conn.createStatement();
      results = stmt.executeQuery(query);

      while (results.next()) {
        
        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        Company company = mapper.mapRow(results);

        companies.add(company);

      }

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);

    } finally {
      sqlUtils.closeConnection(conn);
    }

    return companies;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {

    Company company = null;
    Connection conn = sqlUtils.getConnection();

    String query = "SELECT id, name FROM company WHERE id = ?";
    ResultSet results;

    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);

      results = ps.executeQuery();
      if (results.first()) {

        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        company = mapper.mapRow(results);
      }

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);

    } finally {
      sqlUtils.closeConnection(conn);
    }

    return company;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(long offset, long nbRequested) {
    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();

    if (offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    String query = "SELECT id, name FROM company LIMIT ?, ?";
    String countQuery = "SELECT COUNT(id) AS count FROM company";

    Connection conn = sqlUtils.getConnection();

    try {

      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));

      long currentPage = (long) Math.ceil(offset * 1.0 / nbRequested) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0
          / nbRequested);
     
      searchWrapper.setTotalPage(totalPage);

      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, offset);
      ps.setLong(2, nbRequested);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        
        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        Company company = mapper.mapRow(rs);

        companies.add(company);

      }

      searchWrapper.setResults(companies);

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }

    return searchWrapper;
  }

  @Override
  public void create(Company company) {
    
    String query = "INSERT INTO company(name) VALUES(?)";
    
    Connection conn = sqlUtils.getConnection();
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, company.getName());
      
      ps.executeUpdate();
      
    } catch(SQLException e) {
      
      LOGGER.error("SQLException: {}", e);
      
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
  }

}