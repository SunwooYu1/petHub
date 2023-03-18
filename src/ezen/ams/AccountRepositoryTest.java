package ezen.ams;

import java.io.IOException;
import java.util.List;

public class AccountRepositoryTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		AccountRepository repository = new ObjectAccountRepository();
		// 계좌 등록
		repository.addAccount(new Account("1111-2222-3333", "김재훈", 1111, 100000));
		repository.addAccount(new MinusAccount("1111-3333-4444", "김대출", 1111, 10000, 10000000));
		repository.addAccount(new Account("1111-4444-5555", "김기정", 1111, 100000));
		repository.addAccount(new Account("1111-5555-6666", "김기정", 1111, 100000));
		// 중복 등록 테스트
		repository.addAccount(new Account("1111-4444-5555", "김기정", 1111, 100000));
		System.out.println("계좌 등록 완료");
		
		// 전체 계좌 검색
		List<Account> list = repository.getAccounts();
		for (Account account : list) {
			System.out.println(account);
		}
		
		System.out.println("------------------------------------");
		
		// 계좌번호로 조회
		Account findAccount = repository.findByNumber("1111-2222");
		if(findAccount == null) {
			System.out.println("등록된 계좌가 없습니다");
		}else {
			System.out.println(findAccount);
		}
		
		System.out.println("------------------------------------");
		
		// 예금주명으로 검색
		List<Account> findAccounts = repository.findByName("김기정");
		if(findAccounts.isEmpty()) {
			System.out.println("검색된 계좌가 업습니다");
		}else {
			for (Account account : findAccounts) {
				System.out.println(account);
			}
		}
		
		// 계좌 삭제
		boolean removed = repository.removeAccount("1111-2222");
		if(removed) {
			System.out.println("삭제하였습니다..");
		}else {
			System.out.println("삭제하고자 계좌가 존재하지 않습니다.");
		}
		
		// 파일 저장
		((ObjectAccountRepository)repository).saveFile();
		
		// 테스트 완료 후 화면과 연동합니다..
	}

}
