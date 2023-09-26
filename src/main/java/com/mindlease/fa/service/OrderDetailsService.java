package com.mindlease.fa.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mindlease.fa.exception.ResourceNotFoundException;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mindlease.fa.datatable.Column;
import com.mindlease.fa.datatable.DataTableRequest;
import com.mindlease.fa.datatable.DataTableResult;
import com.mindlease.fa.datatable.Order;
import com.mindlease.fa.model.Archive;
import com.mindlease.fa.model.Company;
import com.mindlease.fa.model.Costs;
import com.mindlease.fa.model.ElectricError;
import com.mindlease.fa.model.FailureMode;
import com.mindlease.fa.model.Location;
import com.mindlease.fa.model.Material;
import com.mindlease.fa.model.Method;
import com.mindlease.fa.model.MethodX;
import com.mindlease.fa.model.OrderDetails;
import com.mindlease.fa.model.Part;
import com.mindlease.fa.model.Personal;
import com.mindlease.fa.model.Priority;
import com.mindlease.fa.model.SamplesRemain;
import com.mindlease.fa.model.Status;
import com.mindlease.fa.model.User;
import com.mindlease.fa.repository.ArchiveRepository;
import com.mindlease.fa.repository.CostsRepository;
import com.mindlease.fa.repository.ElectricErrorRepository;
import com.mindlease.fa.repository.FailureModeRepository;
import com.mindlease.fa.repository.LocationRepository;
import com.mindlease.fa.repository.MaterialRepository;
import com.mindlease.fa.repository.MethodRepository;
import com.mindlease.fa.repository.MethodXRepository;
import com.mindlease.fa.repository.OrderDetailsRepository;
import com.mindlease.fa.repository.PartRepository;
import com.mindlease.fa.repository.PersonalRepository;
import com.mindlease.fa.repository.PriorityRepository;
import com.mindlease.fa.repository.SamplesRemainRepository;
import com.mindlease.fa.repository.StatusRepository;
import com.mindlease.fa.repository.UserRepository;
import com.mindlease.fa.util.FailureAnalysisConstants;
import com.mindlease.fa.util.TabValues;

@Service
public class OrderDetailsService {

	@Autowired
	OrderDetailsRepository orderDetailsRepository;

	@Autowired
	PriorityRepository priorityRepository;

	@Autowired
	MaterialRepository materialRepository;

	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	SamplesRemainRepository samplesRemainRepository;

	@Autowired
	PersonalRepository personalRepository;

	@Autowired
	PartRepository partRepository;

	@Autowired
	ElectricErrorRepository electricErrorRepository;

	@Autowired
	FailureModeRepository failureModeRepository;

	@Autowired
	ArchiveRepository archiveRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	MethodRepository methodRepository;

	@Autowired
	MethodXRepository methodXRepository;

	@Autowired
	CostsRepository costsRepository;

	@Autowired
	UserRepository userRepository;
	  
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    private org.springframework.core.env.Environment environment;


	@Value("${serverAddress}")
	private String serverAddress;

	@Value("${serverLocation}")
	private String serverLocation;

	@Value("${serverPortNumber}")
	private int serverPortNumber;

	@Value("${ftpUserName}")
	private String userName;

	@Value("${ftpUserPassword}")
	private String userPassword;
	  
	private Map<String, Priority> priorityMap = new HashMap<>();
	private Map<String, Location> locationMap = new HashMap<>();
	private Map<String, SamplesRemain> samplesRemainMap = new HashMap<>();
	private Map<String, Material> materialMap = new HashMap<>();
	private Map<String, Status> statusMap = new HashMap<>();
	private Map<String, ElectricError> electricErrorMap = new HashMap<>();
	private Map<String, FailureMode> failureModeMap = new HashMap<>();
	
	private Map<Long, Priority> priorityMapByid = new HashMap<>();

	public List<OrderDetails> getAllOrderDetails() {
		List<OrderDetails> list = new ArrayList<>();
		orderDetailsRepository.findAll().forEach(list::add);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public List<OrderDetails> getAllByCompany(Company company) {
		//return orderDetailsRepository.fetchOrderDetailsInnerJoin();// orderDetailsRepository.fetchOrderDetailsInnerJoin(company);
		List<OrderDetails> list = orderDetailsRepository.fetchOrderDetailsInnerJoin();
		//orderDetailsRepository.findAll().forEach(list::add);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public Optional<OrderDetails> findById(Long id) {
		return orderDetailsRepository.findById(id);
	}

	public OrderDetails getById(Long id) {
		return orderDetailsRepository.getById(id);
	}

	public OrderDetails create(OrderDetails orderDetail) {
		return orderDetailsRepository.save(orderDetail);
	}

	public OrderDetails update(OrderDetails orderDetail) {
		return orderDetailsRepository.save(orderDetail);
	}

	public void deleteById(Long id) {
		orderDetailsRepository.deleteById(id);
	}
	
//	public void updateUserIdById(Integer userId, Long id) {
//		orderDetailsRepository.setUserIdForOrderDetails(userId, id);
//	}

	public List<OrderDetails> findAll() {
		List<OrderDetails> orders = new ArrayList<>();
		orderDetailsRepository.findAll().forEach(orders::add);
		return orders;
	}

	public Page<OrderDetails> searchBy(String sMaterial, String sCustomername, String sPriority, String sStatus,
			String sElectricalError, String sFailureMode, String sArchWaferBox, String sArchPolyBox, PageRequest pageRequest) {
		// return orderDetailsRepository.findAll(pageRequest);
		StringBuilder sb = new StringBuilder("from OrderDetails od where od.id is not null");
		if (sCustomername != null && sCustomername.length() > 0) {
			sb.append(" and od.dbs_ag_name LIKE :name");
		}
		if (sPriority != null && sPriority.length() > 0) {
			sb.append(" and od.dbs_prio LIKE :prio");
		}
		if (sStatus != null && sStatus.length() > 0) {
			sb.append(" and od.dbs_status LIKE :status");
		}
		if (sElectricalError != null && sElectricalError.length() > 0) {
			sb.append(" and od.dbs_elee LIKE :elee");
		}
		if (sFailureMode != null && sFailureMode.length() > 0) {
			sb.append(" and od.dbs_famo LIKE :famo");
		}
		if (sArchWaferBox != null && sArchWaferBox.length() > 0) {
			sb.append(" and od.dbs_fa_archiv_wf LIKE :archivWf");
		}
		if (sArchPolyBox != null && sArchPolyBox.length() > 0) {
			sb.append(" and od.dbs_fa_archiv_ps LIKE :archivPs");
		}
		if (sMaterial != null && sMaterial.length() > 0) {
			sb.append(" and od.dbs_material LIKE :material");
		}
		
		Query query = entityManager.createQuery(sb.toString());
		if (sCustomername != null && sCustomername.length() > 0) {
			query.setParameter("name", "%"+sCustomername+"%");
		}
		if (sPriority != null && sPriority.length() > 0) {
			query.setParameter("prio", "%"+sPriority+"%" );
		}
		if (sStatus != null && sStatus.length() > 0) {
			query.setParameter("status", "%"+sStatus+"%");
		}
		if (sElectricalError != null && sElectricalError.length() > 0) {
			query.setParameter("elee", "%"+sElectricalError+"%");
		}
		if (sFailureMode != null && sFailureMode.length() > 0) {
			query.setParameter("famo", "%"+sFailureMode+"%");
		}
		if (sArchWaferBox != null && sArchWaferBox.length() > 0) {
			query.setParameter("archivWf", "%"+sArchWaferBox+"%");
		}
		if (sArchPolyBox != null && sArchPolyBox.length() > 0) {
			query.setParameter("archivPs", "%"+sArchPolyBox+"%");
		}
		
		if (sMaterial != null && sMaterial.length() > 0) {
			query.setParameter("material", "%"+sMaterial+"%");
		}
		
		List<OrderDetails> list = query.getResultList();
		// Page<OrderDetails> bookPage = new PageImpl<OrderDetails>(list, pageRequest.of(0, pageRequest.getPageSize()), ids.size());
		/*
		 * System.out.println(":::---------ids::::"+ids);
		 * System.out.println(":::---------ids::::"+ids.size()); if (ids.size() > 0) {
		 * ids = new ArrayList<Long>(0); ids.add(0l); }
		 */
		//Page<OrderDetails> list = orderDetailsRepository.findAllByIds(pageRequest,ids);
		Page<OrderDetails> listPage = new PageImpl(list, pageRequest, list.size());
		System.out.println(":::---------list::::"+list.size());
		for (OrderDetails o : list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return listPage;
	}

	public Page<OrderDetails> findAll(PageRequest pageRequest) {
		//return orderDetailsRepository.findAll(pageRequest);
		Page<OrderDetails> list = orderDetailsRepository.findAll(pageRequest);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}
	public Page<OrderDetails> findAllByUserId(PageRequest pageRequest,Integer userId) {
		//return orderDetailsRepository.findAll(pageRequest);
		Page<OrderDetails> list = orderDetailsRepository.findAllByUserId(pageRequest, userId);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public Page<OrderDetails> findAllOpenOrders(PageRequest pageRequest) {
		//return orderDetailsRepository.findAllOpenOrders(pageRequest);
		Page<OrderDetails> list = orderDetailsRepository.findAllOpenOrders(pageRequest);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public Page<OrderDetails> findAllOpenFAOrders(PageRequest pageRequest) {
		//return orderDetailsRepository.findAllOpenFAOrders(pageRequest);
		Page<OrderDetails> list = orderDetailsRepository.findAllOpenFAOrders(pageRequest);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public Page<OrderDetails> findEvaluationOrders(PageRequest pageRequest) {
		//return orderDetailsRepository.findEvaluationOrders(pageRequest);
		Page<OrderDetails> list = orderDetailsRepository.findEvaluationOrders(pageRequest);
		for (OrderDetails o: list) {
			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));
		}
		return list;
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	public Optional<Personal> findPersonByEmail(String email) {
		return personalRepository.findByPERS_MAIL(email);
	}

	public List<Priority> findAllPriorities() {
		List<Priority> list = priorityRepository.findAll();
		priorityMap.clear();
		for (Priority l : list) {
			if (l.getName() != null)
				priorityMap.put(l.getName(), l);
		}
		return list;
	}
	
	public List<Priority> findAllPrioritiesById() {
		List<Priority> list = priorityRepository.findAll();
		priorityMapByid.clear();
		for (Priority l : list) {
			if (l.getName() != null)
				priorityMapByid.put(l.getId(), l);
		}
		return list;
	}

	public List<Location> findAllLocations() {
		List<Location> list = locationRepository.findAll();
		locationMap.clear();
		for (Location l : list) {
			if (l.getName() != null)
				locationMap.put(l.getName(), l);
		}
		return list;
	}
	
	public List<SamplesRemain> findAllSampleRemains() {
		List<SamplesRemain> list = samplesRemainRepository.findAll();
		samplesRemainMap.clear();
		for (SamplesRemain l : list) {
			if (l.getName() != null)
				samplesRemainMap.put(l.getName(), l);
		}
		return list;
	}

	public List<Personal> findAllPersonals() {
		return personalRepository.findAll();
	}
	
	public Optional<Personal> findPersonal() {
		return personalRepository.findById(null);
	}

	public List<Material> findAllMaterials() {
		List<Material> list = materialRepository.findAll();
		materialMap.clear();
		for (Material l : list) {
			if (l.getName() != null)
				materialMap.put(l.getName(), l);
		}
		return list;
	}

	public List<Status> findAllStatuses() {
		List<Status> list = statusRepository.findAll();
		statusMap.clear();
		for (Status l : list) {
			if (l.getName() != null)
				statusMap.put(l.getName(), l);
		}
		return list;
	}

	public List<Part> findAllParts() {
		return partRepository.findAll();
	}

	public Part findPartById(Long partId) {
		return partRepository.findById(partId).orElse(new Part());
	}

	public Part findPartByName(String name) {
		return partRepository.findByName(name);
	}

	public List<ElectricError> findAllElectricErrors() {
		List<ElectricError> list = electricErrorRepository.findAll();
		electricErrorMap.clear();
		for (ElectricError l : list) {
			if (l.getName() != null)
				electricErrorMap.put(l.getName(), l);
		}
		return list;
	}

	public List<FailureMode> findAllFailureModes() {
		List<FailureMode> list = failureModeRepository.findAll();
		failureModeMap.clear();
		for (FailureMode l : list) {
			if (l.getName() != null)
				failureModeMap.put(l.getName(), l);
		}
		return list;
	}
	
	public String getDbs_prio_temp(String key) {
		return key != null && priorityMap.containsKey(key) ? priorityMap.get(key).getNameDe() : null;
	}
	
	public String getDbs_prio_temp_by_id(Long key) {
		return key != null && priorityMapByid.get(key) !=null ? priorityMapByid.get(key).getNameDe() : null;
	}

	public String getDbs_material_temp(String key) {
		return key != null && materialMap.containsKey(key) ? materialMap.get(key).getNameDe() : null;
	}

	public String getDbs_elee_temp(String key) {
		return key != null && electricErrorMap.containsKey(key) ? electricErrorMap.get(key).getNameDe() : null;
	}

	public String getDbs_famo_temp(String key) {
		return key != null && failureModeMap.containsKey(key) ? failureModeMap.get(key).getNameDe() : null;
	}

	
	public String getDbs_location_temp(String key) {
		return key != null && locationMap.containsKey(key) ? locationMap.get(key).getNameDe() : null;
	}

	public String getDbs_status_temp(String key) {
		return key != null && statusMap.containsKey(key) ? statusMap.get(key).getNameDe() : null;
	}

	public List<Method> findAllMethods() {
		return methodRepository.findAll();
	}

	public List<Method> findAllGeneralMethods() {
		return methodRepository.findByGeneral(true);
	}

	public List<Method> findAllHouseMethods() {
		return methodRepository.findByPack(true);
	}

	public List<Method> findAllWaferMethods() {
		return methodRepository.findByWfr(true);
	}

	public List<MethodX> findAllMethodXs() {
		return methodXRepository.findAll();
	}

	public List<Method> findSelectedGeneralMethods(OrderDetails orderDetails) {
		List<Method> methods = new ArrayList<>();
		methodXRepository.findAllGeneralMethods(orderDetails.getId()).forEach(mx -> {
			Optional<Method> m = methodRepository.findById(mx.getMethodId());
			if (m.isPresent())
				methods.add(m.get());
		});
		return methods;
	}

	public List<Method> findSelectedHouseMethods(OrderDetails orderDetails) {

		List<Method> methods = new ArrayList<>();
		methodXRepository.findAllHouseMethods(orderDetails.getId()).forEach(mx -> {
			Optional<Method> m = methodRepository.findById(mx.getMethodId());
			if (m.isPresent())
				methods.add(m.get());
		});
		return methods;
	}

	public List<Method> findSelectedWaferMethods(OrderDetails orderDetails) {
		List<Method> methods = new ArrayList<>();
		methodXRepository.findAllWaferMethods(orderDetails.getId()).forEach(mx -> {
			Optional<Method> m = methodRepository.findById(mx.getMethodId());
			if (m.isPresent())
				methods.add(m.get());
		});
		return methods;
	}

	public List<Archive> findAllArchives() {
		return archiveRepository.findAll();
	}

	public List<Costs> findAllCosts() {
		return costsRepository.findAll();
	}

	public OrderDetails setData(OrderDetails orderDetails, OrderDetails orderDetailsDto, TabValues currentTab) {
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.ORDER)) {
			orderDetails.setDbs_fa_date(orderDetailsDto.getDbs_fa_date());
			orderDetails.setDbs_ag_name(orderDetailsDto.getDbs_ag_name());
			orderDetails.setDbs_material(orderDetailsDto.getDbs_material());
			orderDetails.setDbs_lotid(orderDetailsDto.getDbs_lotid());
			orderDetails.setDbs_wfr(orderDetailsDto.getDbs_wfr());
			orderDetails.setDbs_prio(orderDetailsDto.getDbs_prio());
			orderDetails.setDbs_location(orderDetailsDto.getDbs_location());
			orderDetails.setDbs_step(orderDetailsDto.getDbs_step());
			orderDetails.setDbs_status(orderDetailsDto.getDbs_status());
			orderDetails.setDbs_car(orderDetailsDto.getDbs_car());
		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.GEOMETRY)) {
			orderDetails.setDbs_part(orderDetailsDto.getDbs_part());
		}

		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.REASON)) {
			orderDetails.setDbs_elee(orderDetailsDto.getDbs_elee());
			orderDetails.setDbs_famo(orderDetailsDto.getDbs_famo());
			orderDetails.setDbs_fa_reason(orderDetailsDto.getDbs_fa_reason());
			orderDetails.setDbs_fa_descr(orderDetailsDto.getDbs_fa_descr());
		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.ANALYSIS)) {
			List<MethodX> generalInvestigationMethods_delete = new ArrayList<>();
			List<MethodX> housedExaminationMethods_delete = new ArrayList<>();
			List<MethodX> wafersExaminationMethods_delete = new ArrayList<>();
			List<Method> generalInvestigationMethods_new = new ArrayList<>();
			List<Method> housedExaminationMethods_new = new ArrayList<>();
			List<Method> wafersExaminationMethods_new = new ArrayList<>();
			List<MethodX> generalInvestigationMethods_update = new ArrayList<>();
			List<MethodX> housedExaminationMethods_update = new ArrayList<>();
			List<MethodX> wafersExaminationMethods_update = new ArrayList<>();
			List<MethodX> generalInvestigationMethods = methodXRepository
					.findAllGeneralMethods(orderDetailsDto.getId());
			List<MethodX> housedExaminationMethods = methodXRepository.findAllHouseMethods(orderDetailsDto.getId());
			List<MethodX> wafersExaminationMethods = methodXRepository.findAllWaferMethods(orderDetailsDto.getId());
			for (MethodX mx : generalInvestigationMethods) {
				boolean found = false;
				for (Method m : orderDetailsDto.getGeneralInvestigationMethods()) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (found) {
					generalInvestigationMethods_update.add(mx);
				}
				if (!found) {
					generalInvestigationMethods_delete.add(mx);
				}
			}
			for (Method m : orderDetailsDto.getGeneralInvestigationMethods()) {
				boolean found = false;
				for (MethodX mx : generalInvestigationMethods) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (!found) {
					generalInvestigationMethods_new.add(m);
				}
			}
			for (MethodX mx : generalInvestigationMethods_delete) {
				methodXRepository.delete(mx);
			}

			for (MethodX mx : generalInvestigationMethods_update) {
				methodXRepository.save(mx);
			}
			for (Method m : generalInvestigationMethods_new) {
				MethodX mx = new MethodX();
				mx.setOrder_id(orderDetailsDto.getId());
				mx.setName(m.getName());
				mx.setMethodId(m.getId());
				mx.setGeneral(m.isGeneral());
				mx.setPack(m.isPack());
				mx.setWfr(m.isWfr());
				methodXRepository.save(mx);
			}

			for (MethodX mx : housedExaminationMethods) {
				boolean found = false;
				for (Method m : orderDetailsDto.getHousedExaminationMethods()) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (found) {
					housedExaminationMethods_update.add(mx);
				}
				if (!found) {
					housedExaminationMethods_delete.add(mx);
				}
			}
			for (Method m : orderDetailsDto.getHousedExaminationMethods()) {
				boolean found = false;
				for (MethodX mx : housedExaminationMethods) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (!found) {
					housedExaminationMethods_new.add(m);
				}
			}
			for (MethodX mx : housedExaminationMethods_delete) {
				methodXRepository.delete(mx);
			}

			for (MethodX mx : housedExaminationMethods_update) {
				methodXRepository.save(mx);
			}
			for (Method m : housedExaminationMethods_new) {
				MethodX mx = new MethodX();
				mx.setOrder_id(orderDetailsDto.getId());
				mx.setName(m.getName());
				mx.setMethodId(m.getId());
				mx.setGeneral(m.isGeneral());
				mx.setPack(m.isPack());
				mx.setWfr(m.isWfr());
				methodXRepository.save(mx);
			}

			for (MethodX mx : wafersExaminationMethods) {
				boolean found = false;
				for (Method m : orderDetailsDto.getWafersExaminationMethods()) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (found) {
					wafersExaminationMethods_update.add(mx);
				}
				if (!found) {
					wafersExaminationMethods_delete.add(mx);
				}
			}
			for (Method m : orderDetailsDto.getWafersExaminationMethods()) {
				boolean found = false;
				for (MethodX mx : wafersExaminationMethods) {
					if (m.getId().equals(mx.getMethodId())) {
						found = true;
					}
				}
				if (!found) {
					wafersExaminationMethods_new.add(m);
				}
			}
			for (MethodX mx : wafersExaminationMethods_delete) {
				methodXRepository.delete(mx);
			}

			for (MethodX mx : wafersExaminationMethods_update) {
				methodXRepository.save(mx);
			}
			for (Method m : wafersExaminationMethods_new) {
				MethodX mx = new MethodX();
				mx.setOrder_id(orderDetailsDto.getId());
				mx.setName(m.getName());
				mx.setMethodId(m.getId());
				mx.setGeneral(m.isGeneral());
				mx.setPack(m.isPack());
				mx.setWfr(m.isWfr());
				methodXRepository.save(mx);
			}

		}
		if (currentTab.getValue().equalsIgnoreCase("position")) {
			orderDetails.setDbs_pos_text(orderDetailsDto.getDbs_pos_text());
			orderDetails.setDbs_pos_xcmap(orderDetailsDto.isDbs_pos_xcmap());
			orderDetails.setDbs_pos_01(orderDetailsDto.isDbs_pos_01());
			orderDetails.setDbs_pos_02(orderDetailsDto.isDbs_pos_02());
			orderDetails.setDbs_pos_03(orderDetailsDto.isDbs_pos_03());
			orderDetails.setDbs_pos_04(orderDetailsDto.isDbs_pos_04());
			orderDetails.setDbs_pos_05(orderDetailsDto.isDbs_pos_05());
			orderDetails.setDbs_pos_06(orderDetailsDto.isDbs_pos_06());
			orderDetails.setDbs_pos_07(orderDetailsDto.isDbs_pos_07());
			orderDetails.setDbs_pos_08(orderDetailsDto.isDbs_pos_08());
			orderDetails.setDbs_pos_09(orderDetailsDto.isDbs_pos_09());
			orderDetails.setDbs_pos_10(orderDetailsDto.isDbs_pos_10());
			orderDetails.setDbs_pos_11(orderDetailsDto.isDbs_pos_11());
			orderDetails.setDbs_pos_12(orderDetailsDto.isDbs_pos_12());
			orderDetails.setDbs_pos_13(orderDetailsDto.isDbs_pos_13());
			orderDetails.setDbs_pos_14(orderDetailsDto.isDbs_pos_14());
			orderDetails.setDbs_pos_15(orderDetailsDto.isDbs_pos_15());
			orderDetails.setDbs_pos_16(orderDetailsDto.isDbs_pos_16());
			orderDetails.setDbs_pos_17(orderDetailsDto.isDbs_pos_17());
			orderDetails.setDbs_pos_18(orderDetailsDto.isDbs_pos_18());
			orderDetails.setDbs_pos_19(orderDetailsDto.isDbs_pos_19());
			orderDetails.setDbs_pos_20(orderDetailsDto.isDbs_pos_20());
			orderDetails.setDbs_pos_21(orderDetailsDto.isDbs_pos_21());

		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.LOCATION)) {
			orderDetails.setDbs_remain(orderDetailsDto.getDbs_remain());
		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.PROCESSING)) {
			orderDetails.setDbs_fa_start(orderDetailsDto.getDbs_fa_start());
			orderDetails.setDbs_fa_stop(orderDetailsDto.getDbs_fa_stop());
			orderDetails.setDbs_fa_name(orderDetailsDto.getDbs_fa_name());
			orderDetails.setDbs_fa_text(orderDetailsDto.getDbs_fa_text());
			orderDetails.setDbs_status(orderDetailsDto.getDbs_status());
			orderDetails.setDbs_fa_archiv_wf(orderDetailsDto.getDbs_fa_archiv_wf());
			orderDetails.setDbs_fa_archiv_ps(orderDetailsDto.getDbs_fa_archiv_ps());

		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.COSTS)) {
			orderDetails.setDbs_cost(orderDetailsDto.getDbs_cost());
		}
		if (currentTab.getValue().equalsIgnoreCase(FailureAnalysisConstants.RESULT)) {
			orderDetails.setDbs_res_name(orderDetailsDto.getDbs_res_name());
			orderDetails.setDbs_status(orderDetailsDto.getDbs_status());
			orderDetails.setDbs_res_text(orderDetailsDto.getDbs_res_text());
			orderDetails.setDbs_res_start(orderDetailsDto.getDbs_res_start());
			orderDetails.setDbs_res_stop(orderDetailsDto.getDbs_res_stop());
		}
		if (currentTab.getValue().equalsIgnoreCase("statistics")) {
			orderDetails.setDbs_wait_time1(orderDetailsDto.getDbs_wait_time1());
			orderDetails.setDbs_fa_time(orderDetailsDto.getDbs_fa_time());
			orderDetails.setDbs_wait_time2(orderDetailsDto.getDbs_wait_time2());
			orderDetails.setDbs_res_time(orderDetailsDto.getDbs_res_time());
			orderDetails.setDbs_cpl_time(orderDetailsDto.getDbs_cpl_time());
			orderDetails.setDbs_cost(orderDetailsDto.getDbs_cost());
		}
		return orderDetails;
	}
	
	public Object getOrders(DataTableRequest input) {
		
		PagedListHolder<OrderDetails> page = getpageList(input);
		DataTableResult<OrderDetails> result = new DataTableResult<>();
		result.setRecordsTotal(page.getNrOfElements());
		result.setDraw(input.getDraw());
		result.setRecordsFiltered(page.getNrOfElements());
		result.setData(page.getPageList());
		
		return result;
	}
	
	private PagedListHolder<OrderDetails> getpageList(DataTableRequest input){
		List<OrderDetails> resultList = invokeOrderDetails(input);
		
		PagedListHolder<OrderDetails> pagedList = new PagedListHolder<>(resultList);
		pagedList.setPage(input.getLength() < 0 ? 0 : (input.getStart()/input.getLength()));
		pagedList.setPageSize(input.getLength() < 0 ? resultList.size() : input.getLength());
		
		String sortColumn = "";
		String sortDir = "";
		for(Order order : input.getOrder()) {
			if(StringUtils.hasText(input.getColumns().get(order.getColumn()).getData())) {
				sortDir = order.getDir();
				sortColumn = input.getColumns().get(order.getColumn()).getData();
				break;
			}
		}
		if(StringUtils.hasText(sortColumn)) {
			pagedList.setSort(new MutableSortDefinition (sortColumn, true, "asc".equalsIgnoreCase(sortDir)));
			pagedList.resort();
			pagedList.setPage(input.getLength() < 0 ? 0 : (input.getStart()/input.getLength()));
			pagedList.setPageSize(input.getLength() < 0 ? resultList.size() : input.getLength());
			
		}
		
		return pagedList;
	}
	
	private List<OrderDetails> invokeOrderDetails(DataTableRequest input){
		StringBuilder sb = new StringBuilder("from OrderDetails od where od.id is not null");
		
		String sourceLink = input.getExternalFilter().getOrDefault("sourceLink", "search");
		
		if(sourceLink.equalsIgnoreCase("All Open Orders")) {
			sb.append(" and (od.dbs_status!='Auftrag abgeschlosse' or od.dbs_status!='Order completed')");		
		} else if(sourceLink.equalsIgnoreCase("Open FA Orders")) {
			sb.append(" and (od.dbs_status!='Auftrag abgeschlosse' or od.dbs_status!='Order completed') and (od.dbs_status='FA in Bearbeitung' or od.dbs_status='FA in progress')");
		} else if(sourceLink.equalsIgnoreCase("Evaluation Orders")) {
			sb.append(" and (od.dbs_status='FA erledigt - Bitte Ergebnis bewerte' or od.dbs_status='FA done - please rate the result')");
		} else if(sourceLink.equalsIgnoreCase("Chemical Preparation")){
			sb.append(" and (od.dbs_status='Wartet auf Ätzung' or od.dbs_status='Waiting for Etching')");
		}else if(sourceLink.equalsIgnoreCase("Electrical Measurement")){
			sb.append(" and (od.dbs_status='Wartet auf elektr. Messung' or od.dbs_status='Waiting for electr. Measurement')");
		}
		else if(sourceLink.equalsIgnoreCase("Hotspot / IR / LC")){
			sb.append(" and (od.dbs_status='Wartet auf Hotspot IR/LC' or od.dbs_status='Waiting for Hotspot IR/LC')");}
		else if(sourceLink.equalsIgnoreCase("REM / FIB / EDX")){
			sb.append(" and (od.dbs_status='Wartet auf REM/FIB/EDX' or od.dbs_status='Waiting for REM/FIB/EDX')");}
		else if(sourceLink.equalsIgnoreCase("Cuts")){
			sb.append(" and (od.dbs_status='Wartet auf Schliff' or od.dbs_status='Waiting for cuts')");}
		else if(sourceLink.equalsIgnoreCase("Orders Material /Info Missing")){
			sb.append(" and (od.dbs_status='Material/Info fehlt' or od.dbs_status='Material / info missing')");}


		String[] activeProfiles = environment.getActiveProfiles(); // it will return String Array of all active profile.
		String profileName = "dev";
		if (activeProfiles != null && activeProfiles.length > 0) {
			profileName = activeProfiles[activeProfiles.length-1];
			System.out.println("activeProfile name==:" + profileName);
		}
		System.out.println("profileName==:" + profileName);
		if(StringUtils.hasText(input.getSearch().getValue())) {
//			System.out.println("===global==========:"+input.getSearch().getValue());
			sb.append(" and ( lower(od.dbs_lotid) like :id");
			sb.append(" or CAST(od.id as string) like :id");
			if(profileName.equalsIgnoreCase("dev")) {
				sb.append(" or TO_CHAR(dbs_fa_date, 'MM-dd-yyyy') like :id");
			}else {
				sb.append(" or CONVERT(VARCHAR(10), dbs_fa_date, 110) like :id");
			}
			sb.append(" or lower(od.dbs_ag_name) like :id");
			sb.append(" or lower(od.dbs_material) like :id");
			sb.append(" or lower(od.dbs_prio) like :id");
			sb.append(" or lower(od.dbs_part) like :id");
			sb.append(" or lower(od.dbs_fa_reason) like lower(:id)");
			sb.append(" or lower(od.dbs_fa_name) like lower(:id)");
			sb.append(" or lower(od.dbs_elee) like :id ");
			sb.append(" or lower(od.dbs_famo) like :id )");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sCustomername", ""))) {
			sb.append(" and od.dbs_ag_name LIKE :name");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sPriority", ""))) {
			sb.append(" and od.dbs_prio LIKE :prio");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sStatus", ""))) {
			sb.append(" and od.dbs_status LIKE :status");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sElectricalError", ""))) {
			sb.append(" and od.dbs_elee LIKE :elee");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sFailureMode", ""))) {
			sb.append(" and od.dbs_famo LIKE :famo");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sArchWaferBox", ""))) {
			
			sb.append(" and od.dbs_fa_archiv_wf LIKE :archivWf");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sArchPolyBox", ""))) {
			sb.append(" and od.dbs_fa_archiv_ps LIKE :archivPs");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sMaterial", ""))) {
			sb.append(" and od.dbs_material LIKE :material");
		}
		if(input.getExternalFilter().getOrDefault("isAdmin", "N").equals("N") && (!StringUtils.hasText(sourceLink) || sourceLink.equalsIgnoreCase("search")) ) {
			sb.append(" and od.user.id = :userId");
		}
		
		  String columnVal = null;
		 
		 for(Column col : input.getColumns()) {
			 if (StringUtils.hasText(col.getSearch().getValue())) {
					//System.out.println(col.getData()+"===Column=========="+col.getSearch().getValue());
					
					switch (col.getData()) {
					case "id":
						columnVal = col.getSearch().getValue();
						break;
					case "dbs_fa_date":
						//System.out.println("===dbs_fa_date==========");
						if(profileName.equalsIgnoreCase("dev")) {
							sb.append(" and TO_CHAR(dbs_fa_date, 'MM-dd-yyyy') like :" + col.getData());
						}else {
							sb.append(" and CONVERT(VARCHAR(10), dbs_fa_date, 110) like :" + col.getData());
						}
						break;
					default:
						sb.append(" and lower(od." + col.getData() + ") LIKE :" + col.getData());
					}
				}
		 }
		 
		System.out.println("===sb.toString()=========="+sb.toString());
		
		Query query = entityManager.createQuery(sb.toString());
		if(StringUtils.hasText(input.getSearch().getValue())) {
			query.setParameter("id", "%"+input.getSearch().getValue().trim().toLowerCase()+"%");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sCustomername", ""))) {
			query.setParameter("name", "%"+input.getExternalFilter().getOrDefault("sCustomername", "")+"%");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sPriority", ""))) {
			query.setParameter("prio", "%"+input.getExternalFilter().getOrDefault("sPriority", "")+"%" );
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sStatus", ""))) {
			query.setParameter("status", "%"+input.getExternalFilter().getOrDefault("sStatus", "")+"%");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sElectricalError", ""))) {
			query.setParameter("elee", "%"+input.getExternalFilter().getOrDefault("sElectricalError", "")+"%");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sFailureMode", ""))) {
			query.setParameter("famo", "%"+input.getExternalFilter().getOrDefault("sFailureMode", "")+"%");
		}
		
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sArchWaferBox", ""))) {
			query.setParameter("archivWf", "%"+input.getExternalFilter().getOrDefault("sArchWaferBox", "")+"%");
		}
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sArchPolyBox", ""))) {
			query.setParameter("archivPs", "%"+input.getExternalFilter().getOrDefault("sArchPolyBox", "")+"%");
		}
		
		if (StringUtils.hasText(input.getExternalFilter().getOrDefault("sMaterial", ""))) {
			query.setParameter("material", "%"+input.getExternalFilter().getOrDefault("sMaterial", "")+"%");
		}
		if(input.getExternalFilter().getOrDefault("isAdmin", "N").equals("N") && (!StringUtils.hasText(sourceLink) || sourceLink.equalsIgnoreCase("search"))) {
			query.setParameter("userId", Integer.valueOf(input.getExternalFilter().getOrDefault("userId", "0")));
		}
		input.getColumns().stream().forEach(col -> {
			if (StringUtils.hasText(col.getSearch().getValue()) && !col.getData().contentEquals("id")) {
				query.setParameter(col.getData(), "%" + col.getSearch().getValue().trim().toLowerCase() + "%");
			}
		});
		
		List<OrderDetails> ordersList = query.getResultList();
		List<OrderDetails> targetrdersList = null;

		if (!StringUtils.isEmpty(columnVal) && isNumber(columnVal)) {
			targetrdersList = new ArrayList<>();
			for (OrderDetails order : ordersList) {
				if (order.getId() >= getIdValue(columnVal) && order.getId() < getIdMaxValue(columnVal)) {
					targetrdersList.add(order);
				}
			}
		} else {
			targetrdersList = ordersList;
		}

		for (OrderDetails o : targetrdersList) {

			o.setDbs_elee_temp(getDbs_elee_temp(o.getDbs_elee()));
			o.setDbs_location_temp(getDbs_location_temp(o.getDbs_location()));
			o.setDbs_famo_temp(getDbs_famo_temp(o.getDbs_famo()));
			o.setDbs_status_temp(getDbs_status_temp(o.getDbs_status()));
			o.setDbs_material_temp(getDbs_material_temp(o.getDbs_material()));
			o.setDbs_prio_temp(getDbs_prio_temp(o.getDbs_prio()));

		}
		return targetrdersList;
	}
	
	private long getIdValue(String id) {
		int len = id.length();
		try {
		long searchId = Long.parseLong(id);
		if (len == 1) {
			searchId = searchId * 1000;
		} else if (len == 2) {
			searchId = searchId * 100;
		} else if (len == 3) {
			searchId = searchId * 10;
		}
		return searchId;
		}catch (Exception e) {
			return 0;
		}
	}
	
	private long getIdMaxValue(String id) {
		int len = id.length();
		try {
		long searchId = Long.parseLong(id);
		searchId= searchId+1;
		if (len == 1) {
			searchId = searchId * 1000;
		} else if (len == 2) {
			searchId = searchId * 100;
		} else if (len == 3) {
			searchId = searchId * 10;
		}
		return searchId;
		}catch (Exception e) {
			return 0;
		}
	}
	
	private boolean isNumber(String strNum) {
		if (StringUtils.isEmpty(strNum)) {
	        return false;
	    }
	    try {
	        Long.parseLong(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public String connectFtpServer(String orderNumber) throws IOException, ResourceNotFoundException {

		FTPClient ftpClient = new FTPClient();

		ftpClient.connect(serverAddress, serverPortNumber);

		ftpClient.login(userName, userPassword);

		ftpClient.enterLocalPassiveMode();

		String[] directories = ftpClient.listNames();

		boolean found = false;

		for(String directory: directories) {


			if (directory.equalsIgnoreCase(orderNumber)) {
				found = true;
				break;
			}

		}
		if(found) {
			Runtime.getRuntime().exec("explorer " + serverLocation + orderNumber );


			return "found";
		}
		else {
			return "not found";
		}



	}

	public Optional<Personal> findPersonByShort(String name) {
		return personalRepository.findByPERS_SHORT(name);
	}
}
