package k2dparser.jsfBean;

import k2dparser.data.OrgunitsUpdater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
 
@ManagedBean
@RequestScoped
public class PickListView {
	
    private DualListModel<String> facilities;//to store facilities names
    private DualListModel<String> dataSets;//to store datasets name
    

    //hasmap to store <orgUnitId,orgUnitName> pair
	LinkedHashMap<String, String> facilitiesMap;
    private Date startDate;
    private Date endDate;
     
    @PostConstruct
    public void init() {
    	facilitiesMap = new LinkedHashMap<String, String>();    	
    	
    	
    	//get orgunits from OrgunitUpdater's fectOrgnunits method
    	facilitiesMap = OrgunitsUpdater.fetchOrgunits();
        List<String> facilitiesSource = new ArrayList<String>();
        List<String> facilitiesTarget = new ArrayList<String>();
        
        //extract orgunit names from the map
        for(Object key : facilitiesMap.keySet()){
        	facilitiesSource.add(facilitiesMap.get(key));
        }
         
        facilities = new DualListModel<String>(facilitiesSource, facilitiesTarget);
        
        //DataSets
        List<String> dataSetsSource = new ArrayList<String>();
        List<String> dataSetsTarget = new ArrayList<String>();
        dataSetsSource.add("MOH 711");
        dataSetsSource.add("MOH 731");
        
        dataSets = new DualListModel<String>(dataSetsSource, dataSetsTarget);
         
    }
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
    
    public void submit(ArrayList<String> orgs,ArrayList<String> dSets,Date sDate, Date eDate) {
    	//list to store selected orgunit ids
    	List<String> orgUnitIds = new ArrayList<String>();
    	
    	//traverse the orgs list and get the keys corresponding to orgUnit names
    	for(String orgunit : orgs){
    		String key = (String) getKeyFromValue(facilitiesMap, orgunit);
    		orgUnitIds.add(key);
    	}
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	System.out.println("Selected OrgUnits: "+orgs);
    	System.out.println("Selected OrgUnitIds: "+orgUnitIds);
    	System.out.println("Selected DataSets: "+dSets);
    	System.out.println("Start Date: "+format.format(sDate));
    	System.out.println("End Date: "+format.format(eDate));
        RequestContext requestContext = RequestContext.getCurrentInstance();
         
        requestContext.update("form:displayFacilities");
        requestContext.execute("PF('dlg').show()");        
    }
    
    public Date getstartDate(){
    	return startDate;
    }
    
    public void setstartDate(Date startDate){
    	this.startDate=startDate;
    }
    
    public Date getendDate(){
    	return endDate;
    }
    
    public void setendDate(Date endDate){
    	this.endDate=endDate;
    }
 
    public DualListModel<String> getFacilities() {
        return facilities;
    }
 
    public void setFacilities(DualListModel<String> facilities) {
        this.facilities = facilities;
    }
    
    public DualListModel<String> getDataSets(){
    	return dataSets;
    }
    
    public void setDataSets(DualListModel<String> dataSets){
    	this.dataSets=dataSets;
    }
     
   /* public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append(((Theme) item).getName()).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    } */
 
    /*public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }*/
     
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }
    
    public Object getKeyFromValue(Map<String, String> map, Object value){
    	for(Object o: map.keySet()){
    		if(map.get(o).equals(value))
    			return o;
    	}
    	return null;
    }

}
