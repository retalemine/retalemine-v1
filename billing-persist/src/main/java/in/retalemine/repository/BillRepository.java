package in.retalemine.repository;

import in.retalemine.entity.Bill;

import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends FeaturedRepository<Bill, String>,
		BillBaseRepository<Bill, String> {

}
