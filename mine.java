package test;
import java.util.*;
public class mine {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int tate=0,yoko=0,bomb=0;
		while(tate<2) {
			System.out.println("縦の長さ?(2以上の自然数)");
			tate=scan.nextInt();
		}
		while(yoko<2) {
			System.out.println("横の長さ?(2以上の自然数)");
			yoko=scan.nextInt();
		}
		while(bomb<1||bomb>tate*yoko) {
			System.out.println("地雷は何個?(縦×横未満の自然数)");
			bomb=scan.nextInt();
		}
		int box[][]=new int[tate+2][yoko+2];//地雷場所//[y=tate][x=yoko]	
		int hint[][]=new int[tate+2][yoko+2];//クリック後のヒント(周りの爆弾の数)表示用		
		for(int i=0;i<tate+2;i++) {//現状表示(ここでは初期状態)
			for(int j=0;j<yoko+2;j++) {
				if(i==0||i==tate+1||j==0||j==yoko+1) {
					System.out.print("■");
				}else{
					System.out.print("□");
				}
			}
			System.out.println();
		}
		int field[][]=new int[tate+2][yoko+2];//現在の表示予定場所、 1のとき表示
		int clicked=0;//クリックした回数
		long start=0;//開始時刻
		//ここで入力
		while(true){
			int clickx=0,clicky=0;			
			while(clicky<1||clicky>tate) {
				System.out.println("開く縦の位置?");
				clicky = scan.nextInt();
			}
			while(clickx<1||clickx>yoko) {
				System.out.println("開く横の位置?");
				clickx = scan.nextInt();
			}
			if(clicked==0) {
				start = System.currentTimeMillis();//計測開始
				for(int i=1;i<=bomb;i++) {
					int x=(int)(Math.random()*(yoko));
					int y=(int)(Math.random()*(tate));
					if(box[y+1][x+1]==1||(y+1==clicky&&x+1==clickx)) {//すでに置いてある場所であればやり直し//初手爆弾を回避
						i--;
					}else {
						box[y+1][x+1]=1;
					}
				}
				for(int i=1;i<=tate;i++) {
					for(int j=1;j<=yoko;j++) {
						hint[i][j]=box[i-1][j-1]+box[i][j-1]+box[i+1][j-1]+box[i-1][j]+box[i+1][j]+box[i-1][j+1]+box[i][j+1]+box[i+1][j+1];//ヒント
					}
				}
			}
			if(box[clicky][clickx]==1) {//爆弾クリック時
				for(int i=0;i<=tate+1;i++) {
					for(int j=0;j<=yoko+1;j++) {
						if(i==0||i==tate+1||j==0||j==yoko+1) {
							System.out.print("■");
						}else if(box[i][j]==1) {
							System.out.print("x");
						}else {
							System.out.print(hint[i][j]);
						}
					}
					System.out.println();
				}
				System.out.println("GAMEOVER");
				break;
			}
			click(field,hint,clicky,clickx,tate,yoko);//クリック場所のヒントが0の場合再帰
			int opened=0;//開けた数
			for(int i=1;i<=tate;i++) {
				for(int j=1;j<=yoko;j++) {
					if(field[i][j]==1) {
						opened++;
					}
				}
			}
			if(tate*yoko-opened==bomb) {//クリア判定	
				long finish = System.currentTimeMillis();
				for(int i=0;i<=tate+1;i++) {
					for(int j=0;j<=yoko+1;j++) {
						if(i==0||i==tate+1||j==0||j==yoko+1) {
							System.out.print("■");
						}else if(box[i][j]==1) {
							System.out.print("x");
						}else {
							System.out.print(hint[i][j]);
						}
					}
					System.out.println();
				}
				System.out.println("GAMECLEAR!　Cleartime:"+(double)(finish-start)/1000+"秒");
				break;
			}
			for(int i=0;i<tate+2;i++) {//現状表示
				for(int j=0;j<yoko+2;j++) {
					if(i==0||i==tate+1||j==0||j==yoko+1) {
						System.out.print("■");
					}else if(field[i][j]==0) {
						System.out.print("□");
					}else if(box[i][j]==1) {
						System.out.print("x");
					}else{
						System.out.print(hint[i][j]);
					}
				}
				System.out.println();
			}
			clicked++;
		}
		scan.close();
	}
	static void click(int field[][],int hint[][],int y,int x,int tate,int yoko) {//クリック場所が0のときに周りのマスも開ける関数
		field[y][x]=1;//ここに0のときの挙動　関数作って再帰的if(y!=0&&y!=tate&&x!=0&&x!=yoko+1&&field[y][x]==0)
		//yxクリック、yxfieldを1に、yxが0かつ周りが外枠でないかつ未表示かつ(hint1以上ならyxfieldを1、0なら関数)
		if(hint[y][x]==0) {
			if(y-1!=0&&x-1!=0&&field[y-1][x-1]==0) {//左上
				if(hint[y-1][x-1]==0) {
					click(field,hint,y-1,x-1,tate,yoko);
				}else {
					field[y-1][x-1]=1;
				}
			}
			if(x-1!=0&&x-1!=yoko+1&&field[y][x-1]==0) {//左
				if(hint[y][x-1]==0) {
					click(field,hint,y,x-1,tate,yoko);
				}else {
					field[y][x-1]=1;
				}
			}if(y+1!=tate+1&&x-1!=0&&field[y+1][x-1]==0) {//左下
				if(hint[y+1][x-1]==0) {
					click(field,hint,y+1,x-1,tate,yoko);
				}else {
					field[y+1][x-1]=1;
				}
			}if(y-1!=0&&field[y-1][x]==0) {//上
				if(hint[y-1][x]==0) {
					click(field,hint,y-1,x,tate,yoko);
				}else {
					field[y-1][x]=1;
				}
			}if(y+1!=tate+1&&field[y+1][x]==0) {//下
				if(hint[y+1][x]==0) {
					click(field,hint,y+1,x,tate,yoko);
				}else {
					field[y+1][x]=1;
				}
			}if(y-1!=0&&x+1!=yoko+1&&field[y-1][x+1]==0) {//右上
				if(hint[y-1][x+1]==0) {
					click(field,hint,y-1,x+1,tate,yoko);
				}else {
					field[y-1][x+1]=1;
				}
			}if(x+1!=yoko+1&&field[y][x+1]==0) {//右
				if(hint[y][x+1]==0) {
					click(field,hint,y,x+1,tate,yoko);
				}else {
					field[y][x+1]=1;
				}
			}if(y+1!=tate+1&&x+1!=yoko+1&&field[y+1][x+1]==0) {//右下
				if(hint[y+1][x+1]==0) {
					click(field,hint,y+1,x+1,tate,yoko);
				}else {
					field[y+1][x+1]=1;
				}
			}
		}
	}
}
