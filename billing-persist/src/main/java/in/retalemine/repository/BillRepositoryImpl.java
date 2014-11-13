package in.retalemine.repository;

import in.retalemine.entity.Bill;
import in.retalemine.util.BillNoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;

public class BillRepositoryImpl implements BillBaseRepository<Bill, Integer> {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Bill insert(Bill entity) {

		boolean saveFlag = false;
		int MAX_RETRY = 10;

		if (null != entity.getBillNo()) {
			throw new DuplicateKeyException(entity.getClass().getSimpleName()
					+ " contains BillNo prepopulated as " + entity.getBillNo());
		}
		if (null != entity.getId()) {
			throw new DuplicateKeyException(entity.getClass().getSimpleName()
					+ " contains ID prepopulated as " + entity.getId());
		}

		for (int retry = 1, i = 0; retry <= MAX_RETRY && !saveFlag; retry++) {
			entity.setBillNo(BillNoUtil
					.generateBillNo(BillNoUtil.suffixLen + i));
			try {
				mongoTemplate.insert(entity);
				saveFlag = true;
			} catch (DuplicateKeyException e) {
				if (retry % 5 == 0) {
					i++;
				}
				if (retry == MAX_RETRY) {
					throw new DuplicateKeyException(entity.getClass()
							.getSimpleName()
							+ " exhausted insert retry attempt");
				}
			}
		}

		return entity;
	}
}
