package ezen.ams;

import java.util.List;

public class JdbcAccountRepositoryExample {

	public static void main(String[] args) {
		try {
			AccountRepository repository = new JdbcAccountRepository();
			int count = repository.getCount();
			System.out.println("등록된 계좌수 : " + count);
			
			// 신규계좌 등록 테스트
//			Account account = new Account("2222-2222-3333", "홍길동", 1111, 10000000);
//			repository.addAccount(account);
			
//			MinusAccount ma = new MinusAccount("5555-3333-4444", "박찬호", 1111, 10000, 100000000);
//			repository.addAccount(ma);
			
			// 전체계좌 목록 조회 테스트
			List<Account> list = repository.getAccounts();
			for (Account acc : list) {
				System.out.println(acc);
			}
			
			// 계좌번호 검색 테스트
			System.out.println("--------- 계좌번호로 검색 -----------");
			Account account = repository.findByNumber("5555-3333-4444");
			if(account != null) {
				System.out.println(account);
			}else {
				System.out.println("검색된 계좌가 존재하지 않습니다..");
			}
			
			// 계좌번호 검색 테스트
			System.out.println("--------- 예금주명으로 검색 -----------");
			List<Account> list2 = repository.findByName("김");
			if(!list2.isEmpty()) {
				for (Account acc : list2) {
					System.out.println(acc);
				}
			}else {
				System.out.println("검색된 계좌가 존재하지 않습니다..");
			}
			
			// 계좌번호로 삭제 테스트
			boolean deleted = repository.removeAccount("2222-2222-3333");
			if(deleted) {
				System.out.println("삭제 완료..");
			}else {
				System.out.println("삭제하고자 하는 계좌가 존재하지 않습니다..");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
