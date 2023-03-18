package ezen.ams;

/**
 * Account 클래스를 상속받는 마이너스 계좌
 * @author 김기정
 * @Date   2023. 1. 4.
 */
public class MinusAccount extends Account  { 
	// 새롭게 추가된 속성
	private long borrowMoney;
	
	public MinusAccount() {}
	public MinusAccount(String accountNumber, String accountOwner, int password, long restMoney, long borrowMoney) {
		super(accountNumber, accountOwner, password, restMoney);
		this.borrowMoney = borrowMoney;
	}
	public long getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	// 잔액(restMoney - borrowMoney) 재정의(Overriding)
	public long getRestMoney() {
		return super.getRestMoney() - borrowMoney;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\t" + borrowMoney ;
	}

}














