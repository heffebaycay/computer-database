package fr.heffebaycay.cdb.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.Constants;

@Controller
@RequestMapping("/computers/add")
public class AddComputerController {

  private static final Logger LOGGER = LoggerFactory
                                         .getLogger(AddComputerController.class
                                             .getSimpleName());

  private static final String ATTR_COMPANIES = "companies";
  private static final String ATTR_VALIDATION_FAILED = "msgValidationFailed";
  
  
  @Autowired
  private IComputerService    mComputerService;
  @Autowired
  private ICompanyService     mCompanyService;
  @Autowired
  private ComputerMapper      computerMapper;
  @Autowired
  private CompanyMapper       companyMapper;

  public AddComputerController() {

  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(ComputerDTO computerDTO, ModelMap map) {
    LOGGER.debug("Call to AddComputerController::doGet()");

    // The view requires the list of all companies
    map.addAttribute(ATTR_COMPANIES, companyMapper.toDTO(mCompanyService.findAll()));

    return Constants.JSP_ADD_COMPUTER;
  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(@Valid ComputerDTO computerDTO, BindingResult result, ModelMap map,
      RedirectAttributes redirectAttrs) {

    LOGGER.debug("Call to AddComputerController::doPost()");
    Computer computer = new Computer();

    // Validating company is special as we need to check that the company selected
    // by the user does exist in the data source.
    Company company = null;
    Long companyId = computerDTO.getCompanyId();
    if (companyId > 0) {
      company = mCompanyService.findById(companyId);
      if (company == null) {
        result.addError(new ObjectError("companyId", "Selected company does not exist"));
      }
    }

    if (result.hasErrors()) {
      LOGGER.debug("doPost(): ComputerDTO has errors [JSR 303 Validation].");

      if (LOGGER.isDebugEnabled()) {
        for (ObjectError error : result.getAllErrors()) {
          LOGGER.debug("doPost(): JSR 303 Validation: {}", error.getDefaultMessage());
        }
      }

      map.addAttribute(ATTR_VALIDATION_FAILED, true);

      //return "addComputer";
      return doGet(computerDTO, map);
    } else {
      computer = computerMapper.fromDTO(computerDTO);

      computer.setCompany(company);
      long computerId = mComputerService.create(computer);

      // All done, let's go to the edit page of this computer
      redirectAttrs.addAttribute("id", computerId);
      return "redirect:/computers/edit?id={id}&msg=addSuccess";
    }

  }

}
