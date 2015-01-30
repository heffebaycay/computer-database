package fr.heffebaycay.cdb.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.web.exception.ComputerNotFoundException;
import fr.heffebaycay.cdb.web.exception.ServiceInitializationException;

@Controller
@RequestMapping("/computers/edit")
public class EditComputerController {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerController.class
                                         .getSimpleName());

  @Autowired
  private IComputerService    mComputerService;
  @Autowired
  private ICompanyService     mCompanyService;

  public EditComputerController() {
    super();
  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(Long id, @RequestParam(required = false) String msg,
      ComputerDTO computerDTO, ModelMap map) {

    LOGGER.debug("Received call to EditComputerController::doGet()");

    if (mComputerService == null) {
      LOGGER.error("doGet() : IComputerService instance is null");
      throw new ServiceInitializationException();
    }

    if (mCompanyService == null) {
      LOGGER.error("doGet() : ICompanyService instance is null");
      throw new ServiceInitializationException();
    }

    if ("addSuccess".equals(msg)) {
      map.addAttribute("bAddSuccess", true);
    }

    ComputerDTO computer = ComputerMapper.toDTO(mComputerService.findById(id));
    if (computerDTO == null) {
      // Unable to find any computer based on the given Id
      LOGGER.warn("doGet() : Inexisting computer requested by user");
      throw new ComputerNotFoundException();
    }

    List<CompanyDTO> companies = CompanyMapper.toDTO(mCompanyService.findAll());

    map.addAttribute("companies", companies);
    map.addAttribute("computer", computer);

    return "editComputer";
  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(@Valid ComputerDTO computerDTO, BindingResult result, ModelMap map,
      RedirectAttributes redirectAttrs) {

    LOGGER.debug("Received call to EditComputerController:doPost()");

    LOGGER.debug("doPost() :: ComputerId: " + computerDTO.getId());

    Computer computer = mComputerService.findById(computerDTO.getId());
    if (computer == null) {
      LOGGER.warn("doPost() :: User supplied an Id for a non-existing computer.");
      // 40X ?

    }

    // Storing given value in case something goes wrong and we need to display it back to the user
    map.addAttribute("computerNameValue", computerDTO.getName());
    map.addAttribute("dateIntroducedValue", computerDTO.getIntroduced());
    map.addAttribute("dateDiscontinuedValue", computerDTO.getDiscontinued());
    map.addAttribute("companyIdValue", computerDTO.getCompanyId());

    // Creating the Company object
    Company company = null;

    if (computerDTO.getCompanyId() > 0) {
      company = mCompanyService.findById(computerDTO.getCompanyId());
      if (company == null) {
        LOGGER.warn("doPost() :: Company does not exist");
        result.addError(new ObjectError("companyId", "Selected company does not exist"));
      }
    }

    if (result.hasErrors()) {
      // Validation failed
      LOGGER.debug("doPost(): ComputerDTO has errors [JSR 303 Validation]");

      if (LOGGER.isDebugEnabled()) {
        for (ObjectError error : result.getAllErrors()) {
          LOGGER.debug("doPost(): JSR 303 Validation error: {}", error.getDefaultMessage());
        }
        map.addAttribute("msgValidationFailed", true);
      }

      return doGet(computerDTO.getId(), null, computerDTO, map);

    } else {
      // Validation succeeded
      map.addAttribute("msgSuccess", true);

      ComputerMapper.updateDAO(computer, computerDTO);

      computer.setCompany(company);
      mComputerService.update(computer);
    }

    map.addAttribute("id", computerDTO.getId());

    return doGet(computerDTO.getId(), null, computerDTO, map);

  }

}
