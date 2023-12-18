package com.rgt.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rgt.dto.OrderDTO;
import com.rgt.repository.RgtMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RgtService {
	
	@Autowired
	private RgtMapper rgtMapper;

	@Autowired
	DataSource dataSource;
	
	/**
	 * 과제 1 : RestApi작성 -> 과제 2-1, 2-2 포함  
	 * @param orderDTO
	 * @return
	 */
	public String rgtTest(OrderDTO orderDTO) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(def);
		
		log.debug(" ## OrderDTO INFO : [{}] ", orderDTO.toString());
		
		String result = "";
		try {
			//필수 입력값 체크
			if(orderDTO.getOrderId().isEmpty() || orderDTO.getProductName().isEmpty() || orderDTO.getTableNo().isEmpty() ||
			   orderDTO.getQuantity().isEmpty() || orderDTO.getOrderDate().isEmpty() || orderDTO.getOrderTime().isEmpty() ||
			   orderDTO.getDong().isEmpty() || orderDTO.getHo().isEmpty() || orderDTO.getSeq().isEmpty() || 
			   orderDTO.getOrdererName().isEmpty() ||  orderDTO.getDateTime().isEmpty() ) {
				
				result = "Error";
				return result;
			
			}
			
			// 과제 2-1 : 중복 데이터 처리..
			if(rgtMapper.countOrderId(orderDTO.getOrderId()) > 0){
				return "이미 주문된 번호입니다.";
			}
			
			//주문 정보 저장
			if(rgtMapper.insertOrderInfo(orderDTO) < 0) {
				result = "Error";
				return result;
			}
			
			// 과제 2-2 : 데이터 수정 
			txManager.commit(status);
			int changeName = rgtMapper.updateProductName();
			
			//결과값 ex: 주문번호 : 0007
			result = "주문번호 : " + orderDTO.getOrderId();
			
		}catch(Exception e){
			log.error(" ## ORDER ERROR : [{}], [{}]", e, e.getMessage());
			txManager.rollback(status);
			result = "Error";
		}
		
		return result;
	}
	
	@Autowired
	private AsyncMethods asyncMethods; 
	public String async(String param) {
		//@Async메소드를 호출 할때 같은 클래스에 있는 Async메소드는 비동기적으로 동작하지 않는다.
		//다른 클래스에 만들어서 의존성을 주입 받아 사용돼야 한다.
		//접근제한자를 public으로 만들어야되고 반환타입을 void로 설정해야 된다.
		asyncMethods.asyncMethod(param);
		
		return null;
	}
	
	@Component
	public class AsyncMethods{
		@Async
		public void asyncMethod(String param) {
			try {
				System.out.println(" ## ASYNC TEST START !! param = " + param);
				Thread.sleep(4000);
				System.out.println(" ## ASYNC TEST STOP .. param = " + param);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
