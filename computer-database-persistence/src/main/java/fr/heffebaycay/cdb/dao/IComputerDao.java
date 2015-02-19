package fr.heffebaycay.cdb.dao;

import java.util.List;

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Computer DAO contract
 *
 */
public interface IComputerDao {

  /**
   * This method returns the list of all computers stored in the data source
   * 
   * @return The list of all computers
   */
  List<Computer> findAll();

  /**
   * Find a given Computer from the data source
   * 
   * @param id The Id of the Computer object that should be returned
   * @return An instance of Computer or <strong>null</strong> if there's no match
   */
  Computer findById(long id);

  /**
   * This method removes a Computer from the data source based on its Id
   * 
   * @param id          Id of the computer object to be removed
   * @return            boolean indicating success (true) or failure (false) of the removal operation
   */
  boolean remove(long id);

  /**
   * This methods removes a list of computers from the data source based on their
   * identifiers
   * 
   * @param ids         The list of the identifiers of the computers that should be removed
   */
  void remove(List<Long> ids);

  /**
   * Create a Computer in the data source based on an instance of Computer
   * 
   * @param computer The computer object that should be persisted
   * 
   * @return                            id of the created computer
   * @throws IllegalArgumentException   If the <strong>computer</strong> parameter is null
   */
  long create(Computer computer);

  /**
   * Update an already existing Computer in the data source
   * 
   * @param computer                    The computer object that should be updated
   * @throws IllegalArgumentException   If the <strong>computer</strong> parameter is null
   */
  void update(Computer computer);

  /**
   * Queries the data source for a given number of elements starting at a given offset defined within the <strong>request</strong> parameter.
   * 
   * @param request                     Object containing the details of the findAll() request (offset, number of elements)
   * @return                            A {@link fr.heffebaycay.cdb.wrapper.SearchWrapper} object containing the collection of Computer object, as well as information about the page
   * @throws IllegalArgumentException   If the <strong>request</strong> parameter is null
   */
  SearchWrapper<Computer> findAll(ComputerPageRequest request);

  /**
   * Removes all computers matching a given company from the data source
   * 
   * @param companyId        The id of the Company for which associated Computers should be removed
   * @return                 The number of elements affected by the operation.
   */
  int removeForCompany(long companyId);

  /**
   * Queries the data source for a given number of elements starting at the offset defined within the <strong>request</strong> parameter.
   * 
   * The method will attempt to find Computer objects whose name or company name (if applicable) matches the searchQuery attribute of <strong>request</strong>.
   * 
   * @param request                     Object containing the details of the findByName() request, including the search keyword
   * @return                            A {@link fr.heffebaycay.cdb.wrapper.SearchWrapper} object containing the collection of Computer object, as well as information about the page
   * @throws IllegalArgumentException   If the <strong>request</strong> parameter is null
   */
  SearchWrapper<Computer> findByName(ComputerPageRequest request);

}
