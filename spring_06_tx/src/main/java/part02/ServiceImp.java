package part02;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import model.MemDAO;
import model.MemDTO;

public class ServiceImp implements Service{
	
	private MemDAO memDAO;
	private TransactionTemplate transacionTemplate;
	
	
	public ServiceImp() {
	
	}
		
	public void setMemDAO(MemDAO memDAO) {
		this.memDAO = memDAO;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transacionTemplate = transactionTemplate;
	}
	
	@Override
	public void insertProcess() {
		String result = transacionTemplate.execute(new TransactionCallback<String>() {
			@Override
			public String doInTransaction(TransactionStatus status) {
	
				try {
				memDAO.insertMethod(new MemDTO(44, "용팔이", 50, "경기"));
				memDAO.insertMethod(new MemDTO(45, "유대위", 20, "대전"));
				return "OK";
				} catch(Exception ex) {
					status.setRollbackOnly();
				return ex.toString();
				}
			}
		});
		System.out.println("Result : " + result);
	} // end insertProcess
}