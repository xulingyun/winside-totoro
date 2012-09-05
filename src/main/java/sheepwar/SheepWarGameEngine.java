package sheepwar;

import javax.microedition.midlet.MIDlet;

import cn.ohyeah.stb.game.GameCanvasEngine;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;

/**
 * 游戏引擎
 * 
 * @author Administrator
 */
public class SheepWarGameEngine extends GameCanvasEngine implements Common {
	public Role own;      //玩家操控的羊
	public Role wolf;     //npc
	public Role buble;     //气球

	public static boolean isSupportFavor = false;
	public static int ScrW = 0;
	public static int ScrH = 0;
	public CreateRole createRole;
	public Weapon weapon;
	public Attacks attacks;
	
	public static int bombAmount;		//发射子弹数量
	public static SheepWarGameEngine instance = buildGameEngine();

	private static SheepWarGameEngine buildGameEngine() {
		return new SheepWarGameEngine(SheepWarMIDlet.getInstance());
	}

	private SheepWarGameEngine(MIDlet midlet) {
		super(midlet);
		setRelease(true);
		ScrW = screenWidth;
		ScrH = screenHeight;
	}

	public ShowGame showGame;
	public int status;
	public int mainIndex, playingIndex;
	
	protected void loop() {

		switch (status) {
		case STATUS_INIT: // 初始化
			init();
			break;
		case STATUS_MAIN_MENU: // 主菜单
			processGameMenu();
			break;
		case STATUS_GAME_PLAYING: // 游戏中
			processGamePlaying();// 游戏中的操作
			break;
		case STATUS_GAME_SHOP: // 道具商城
			 //processShop();
			break;
		case STATUS_GAME_ARCHI:// 游戏成就
			//processArchi();
			break;
		case STATUS_GAME_RANKING:// 游戏排行
			 //processRanking();
			break;
		case STATUS_GAME_HELP:// 游戏帮助
			 //processHelp();
			break;
		}

		switch (status) {
		case STATUS_INIT:
			showInit(g);// 画出初始化的动画
			break;
		case STATUS_MAIN_MENU:
			showGameMenu(g);
			break;
		case STATUS_GAME_PLAYING:
			showGamePlaying(g);
			break;
		case STATUS_GAME_SHOP: // 道具商城
			 //showGameShop(g);
			break;
		case STATUS_GAME_ARCHI:// 游戏成就
			 //showGameArchi(g);
			break;
		case STATUS_GAME_RANKING:// 游戏排行
			//showRanking(g);
			break;
		case STATUS_GAME_HELP:// 游戏帮助
			//showHelp(g);
			break;
		}
	}

	private void moveRole(int towards) {
		switch (towards) {
		case 0: // 往上--主角
			if(own.mapy>=164){//吊篮上极限坐标164
				own.mapy -= own.speed;
			}
			break;
		case 1: // 往下--主角
			own.direction = 1;
			if(own.mapy + own.height<460){//吊篮下极限坐标原460--问题：增加Y坐标极限会出现area out of Iamge ?
				own.mapy += own.speed;
			}
			break;
		}
	}

	private void showInit(SGraphics g) {
		/*
		 * g.setColor(0X000000); g.setClip(0, 0, 100, 100);
		 * g.setColor(0Xffffff); g.drawString("加载中,请稍后...", 300, 260, 10);
		 */
	}

	private void init() {
		status = STATUS_MAIN_MENU;                            // 进入游戏菜单
		showGame = new ShowGame(this);
		weapon = new Weapon();
		attacks = new Attacks(this);
		createRole = new CreateRole();
		own = createRole.createSheep();
		createRole.createWolf();
	}

	private void showGameMenu(SGraphics g) {
		showGame.drawMainMenu(g, mainIndex);
	}

	private void showGamePlaying(SGraphics g) {
		showGame.drawGamePlaying(g, playingIndex,own);
		createRole.showSheep(g,own);//动态的羊
		int len = CreateRole.npcs.size();
		for(int i=0;i<len;i++){
			wolf = (Role) CreateRole.npcs.elementAt(i);
			buble=(Role)CreateRole.bubles.elementAt(i);
			createRole.showWolf(g, wolf,buble);
		}
		weapon.showBomb(g);
	}
	
	private void processGameMenu() {
		if (keyState.contains(KeyCode.UP)) {
			keyState.remove(KeyCode.UP);
			mainIndex = (mainIndex + 6 - 1) % 6;
		} else if (keyState.contains(KeyCode.DOWN)) {
			keyState.remove(KeyCode.DOWN);
			mainIndex = (mainIndex + 1) % 6;
		} else if (keyState.contains(KeyCode.OK)) {
			keyState.remove(KeyCode.OK);
			showGame.clearMainMenu();
			processSubMenu();
		}
		if (keyState.contains(KeyCode.BACK)) { // 返回键直接退出
			keyState.remove(KeyCode.BACK);
			showGame.clearMainMenu();
			exit = true;
		}
	}

	private void processGamePlaying() {
		
		if (keyState.containsMoveEventAndRemove(KeyCode.UP)) {
			moveRole(0);//
		} else if (keyState.containsMoveEventAndRemove(KeyCode.DOWN)) {
			moveRole(1);
		} else if (keyState.contains(KeyCode.OK)) { // 普通攻击
			keyState.remove(KeyCode.OK);
			weapon.createBomb(own, 2);
//			weapon.bombAmount++;  //射出的武器数目增加
			
		}else if(keyState.contains(KeyCode.NUM1)){    //时光闹钟
			keyState.remove(KeyCode.NUM1);
		}else if(keyState.contains(KeyCode.NUM2)){ //捕狼网
			keyState.remove(KeyCode.NUM2);
		}else if(keyState.contains(KeyCode.NUM3)){//盾牌
			keyState.remove(KeyCode.NUM3);
		}else if(keyState.contains(KeyCode.NUM4)){//激光枪
			keyState.remove(KeyCode.NUM4);
		}else if(keyState.contains(KeyCode.NUM5)){//驱散竖琴
			keyState.remove(KeyCode.NUM5);
		}else if(keyState.contains(KeyCode.NUM6)){//
			keyState.remove(KeyCode.NUM6);
		}else if(keyState.contains(KeyCode.NUM7)){
			keyState.remove(KeyCode.NUM7);
		}else if(keyState.contains(KeyCode.NUM8)){//木偶->可以增加一条生命
			keyState.remove(KeyCode.NUM8);
		}else if(keyState.contains(KeyCode.NUM9)){
			keyState.remove(KeyCode.NUM9);
			showGame.clearGamePlaying();
			status = STATUS_GAME_HELP;
		}
		else if (keyState.containsAndRemove(KeyCode.NUM0)) { // 按0返回-----完善后应改为0键暂停！but 帮助中的操作声名却是退出游戏
			showGame.clearGamePlaying();
			status = STATUS_MAIN_MENU;
		}
		if(timePass(10000)){
			createRole.createWolf();
		}
		attacks.bompAttack(wolf, own);
		
	}
	
	private long recordTime;
	private boolean timePass(int millisSeconds) {
		long curTime = System.currentTimeMillis();
		if (recordTime <= 0) {
			recordTime = curTime;
		}
		else {
			if (curTime-recordTime >= millisSeconds) {
				recordTime = 0;
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/*注意和界面按钮的顺序一致*/
	private void processSubMenu() {
		if (mainIndex == 0) { //新游戏
			status = STATUS_GAME_PLAYING;
			
		} else if (mainIndex == 1) {// 道具商城
			StateShop ss =  new StateShop();
			ss.processShop();
			
		} else if (mainIndex == 2){ //成就系统
			StateAttainment sa = new StateAttainment();
			sa.processAttainment();
			
		} else if (mainIndex == 3) {// 排行榜
			StateRanking sr = new StateRanking();
			sr.processRanking();
			
		} else if (mainIndex == 4) {// 游戏帮助
			StateHelp sh = new StateHelp();
			sh.processHelp();
			
		}else if(mainIndex==5){//退出游戏
			exit = true;
		} 
		showGame.clearMainMenu();
	}
}
