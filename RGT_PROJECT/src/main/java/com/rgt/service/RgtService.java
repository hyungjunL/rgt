package com.rgt.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
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
	
}
