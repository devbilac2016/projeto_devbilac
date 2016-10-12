package Screens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.DevBilac;

import Scenes.HudUs07;
import Sprites.BarraLateral;
import Sprites.Botao;
import Sprites.Circulo;
import Tools.B2WorldCreator;

//Tela destinada ao Jogo da User Story 07.
public class ScreenUs07 implements Screen {
	private HudUs07 hud;
	//Variaveis Box2d
	private World world;
	private Box2DDebugRenderer b2dr;
	private DevBilac game;
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private SpriteBatch batch;
	ArrayList<Circulo> circulos = new ArrayList<Circulo>();
	Vector3 touchPos;
	int tamanhoOriginalH;
	int tamanhoOriginalW;
	BarraLateral BarraLateral;
	Botao botao;
	int Clicked;
	boolean ClickedBotao;
	boolean ClickedCirculo;
	Vector3 AuxV01;
	private BitmapFont currentFont;
	String estrutura;
	boolean jogoPausado;
	int gravarId;
	
	
	public ScreenUs07(DevBilac game){
		//Pega o Tamanho Atual da Tela, Largura e Altura e armazena.
		tamanhoOriginalH = Gdx.graphics.getHeight();
		tamanhoOriginalW = Gdx.graphics.getWidth();
		touchPos = new Vector3();
		batch = new SpriteBatch();
		// Chama o HudRanking que é um 'Display' na tela, onde ficas Labels.
		this.game = game;
		gamecam = new OrthographicCamera();
		world = new World(new Vector2(0,-200 / DevBilac.PPM), true);
		//Alterar para mudar a Gravidade do mundo
		world = new World(new Vector2(0,-200 / DevBilac.PPM), true);
		b2dr = new Box2DDebugRenderer();
		//o 'Metodo' FitViewport faz o redimencionamento de tela.
		gamePort = new FitViewport(DevBilac.V_WIDHT / DevBilac.PPM,DevBilac.V_HEIGHT / DevBilac.PPM,gamecam);
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
		BarraLateral = new BarraLateral();
		BarraLateral.setTexture(new Texture("images/fundo.jpg"));
		BarraLateral.setEstrutura("");
		BarraLateral.setPosition(new Vector3(0,0,0));
		BarraLateral.setAtivo(true);
		botao = new Botao();
		botao.setTexture(new Texture("images/botao.jpg"));
		botao.setPosition(new Vector3(BarraLateral.getTexture().getWidth(),500,0));
		botao.setAtivo(true);
		currentFont = new BitmapFont();
		hud = new HudUs07(game.batch); 
		hud.setTimer(60); //Envia o Tempo que o jogo tera para o Display.
		estrutura = ""
				+ "Se(bolinha = true){ "
				+ "\nganha +1"
				+ "\n"
				+ "\n}senao{"
				+ "\nperde 1 ponto"
				+ "\n"
				+ "\n}";
		jogoPausado = true;
		criarCirculo(false);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void handleInput(float delta){
		BarraAcao();
	}
	public void update(float delta){
		handleInput(delta);
		if(jogoPausado == false){
			hud.update(delta);
		}
		for (Circulo circulo : circulos) {
			if(circulo.isAtivo()){
				circulo.update(delta);
			}
		}
		//Remove Circulos que sairam da Tela
		int removeu = 0;
		for (int i = 0; i < circulos.size(); i++) {
			double aux = circulos.get(i).getPosition().y;
			if(aux <= 0){
				circulos.remove(circulos.get(i));
				removeu++;
			}
		}
		//Adiciona se removeu 1
			for (int i = 0; i < removeu; i++) {
				//circulos.add(criaCirculo(true));
				System.out.println("add");
			}
		//Se Acabou o Jogo, aparece um AVISO e limpa o Array de Circulos.
		if(acabouJogo()){
			for (int i = 0; i < circulos.size(); i++) {
					circulos.remove(circulos.get(i));
			}
			System.out.println("JOGO ACABOU, CIRCULOS REMOVIDOS");
		}
		
		
		
		
		CirculoAcao();
		
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		hud.stage.draw(); 
		batch.begin();
		
		if(BarraLateral.isAtivo()){
			batch.draw(BarraLateral.getTexture(),BarraLateral.getPosition().x, BarraLateral.getPosition().y);
			currentFont.draw(batch,estrutura, 20, 450);
		}
		batch.draw(botao.getTexture(),botao.getPosition().x,botao.getPosition().y);
		
		//Bloco de codigo que roda o Array para desenhar os Objetos
				for (Circulo circulo : circulos) {
					if(circulo.isAtivo()){
						batch.draw(circulo.getTexture(),circulo.getPosition().x,circulo.getPosition().y);
						circulo.getFont().draw(batch, circulo.getMsg(), circulo.getMsgX(), circulo.getMsgY());
					}
				}
				
				batch.draw(new Texture("images\\inativo.png"),90,20);
				batch.draw(new Texture("images\\inativo.png"),90,60);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		currentFont.dispose();
		
	}

	//Metodo Utilizado para pegar as Posições X,Y do Mouse e tratalas adequadamente.
		public Vector3 PositionMouse(){
			Vector3 PositionM = new Vector3();
			touchPos.set(0,Gdx.input.getY(), 0);
	        gamecam.unproject(touchPos);
	        float TouchY = touchPos.y*300;
	        PositionM.x = Gdx.input.getX();
	        PositionM.y = (int)TouchY;
	        float aux01 = (tamanhoOriginalW - Gdx.graphics.getWidth())/2;
	        if(aux01 <=0){
	       	 PositionM.x = Gdx.input.getX()+aux01;
	        }else{
	       	 touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
	       	 gamecam.unproject(touchPos);
	        }
	        System.out.println(PositionM);
			return PositionM;
		}
		
	public void BarraAcao(){
		
		
		if(Gdx.input.isTouched() && acabouJogo() == false){
			if(Clicked == 1){
				AuxV01 = PositionMouse();
					//Verifica se o Mouse esta dentro do Objeto.
					System.out.println(AuxV01);
					if (AuxV01.x >= botao.getPosition().x && AuxV01.x <= (botao.getPosition().x + botao.getTexture().getWidth())){
						if (AuxV01.y >= botao.getPosition().y && AuxV01.y <= (botao.getPosition().y + botao.getTexture().getHeight())){
							System.out.println("Clicou Dentro");
							ClickedBotao = true;
							Clicked++;
						}
						
					}
			}else{
				if(ClickedBotao == true){
					if (BarraLateral.isAtivo()){
						System.out.println("Desativar!!");
						BarraLateral.setAtivo(false);
						botao.setAtivo(false);
						float Aux01 = BarraLateral.getPosition().x-BarraLateral.getTexture().getWidth();
						BarraLateral.setPosition(new Vector3(Aux01,BarraLateral.getPosition().y,0));
						botao.setPosition(new Vector3(0,500,0));
						setViewCirculo(true);
						jogoPausado = false;
					}else{
						System.out.println("Ativar!!");
						BarraLateral.setAtivo(true);
						botao.setAtivo(true);
						BarraLateral.setPosition(new Vector3(0,BarraLateral.getPosition().y,0));
						botao.setPosition(new Vector3(BarraLateral.getTexture().getWidth(),500,0));
						setViewCirculo(false);
						jogoPausado = true;
					}
					ClickedBotao = false;
				}else{Clicked++;}
			}
			

		}else{Clicked = 0;
		}
	}
	
	
	public void CirculoAcao(){
		if(Gdx.input.isTouched() && acabouJogo() == false){
			int auxRemove = 0;
			for (Circulo circulo : circulos) {
				
				if(Clicked == 1){
					AuxV01 = PositionMouse();
					//Verifica se o Mouse esta dentro do Objeto.
					System.out.println(AuxV01);
					if (AuxV01.x >= circulo.getPosition().x && AuxV01.x <= (circulo.getPosition().x + circulo.getTexture().getWidth())){
						if (AuxV01.y >= circulo.getPosition().y && AuxV01.y <= (circulo.getPosition().y + circulo.getTexture().getHeight())){
							System.out.println("Clicou Dentro");
							ClickedCirculo = true;
							System.out.println(circulo.isResposta());
							Clicked++;
							gravarId=auxRemove;
						}	
					}
				}else{
					if(ClickedCirculo == true && auxRemove == gravarId){
					  	if(circulos.get(auxRemove).isResposta() == true){
							Pontuar(2);
							//Ligar Luz VERDE
							//IF VEZES SEGUIDAS, LIGAR ELSE
					  	}else{
							Pontuar(1);
							//Ligar Luz VERMELHA
					  	}
						circulos.remove(circulos.get(auxRemove));
						ClickedCirculo = false;
						break;
					}else{Clicked++;}
				}
				auxRemove++;
			}
		}else{
			Clicked = 0;
		}
	}
	
	
	public void criarCirculo(boolean Ativo) {
		int limiteCirculos = 3;
		for (int i = 0; i < limiteCirculos; i++) {
			Circulo circulo = new Circulo();
			circulo.setId(i);
			circulo.setAtivo(Ativo);
			circulo.setPosition(gerarPosition());
			circulo = gerarVisual(circulo);
			circulo = gerarQuestao(circulo);
			circulos.add(circulo);
		}
	}
	
	public void setViewCirculo(boolean estado){
		for (Circulo circulo : circulos) {
			circulo.setAtivo(estado);
		}
	}
	
	public Vector3 gerarPosition(){
		Vector3 positions[] = {
				new Vector3(50,400,0),new Vector3(550,400,0),new Vector3(1050,400,0),
		};
		Random gerador = new Random();
		int aux = gerador.nextInt(positions.length);
		Vector3 newPosition = new Vector3(positions[aux].x,positions[aux].y,positions[aux].z);
		
		return newPosition;
	}
	public Circulo gerarQuestao(Circulo oldCirculo){
		Circulo circulo = oldCirculo;
		String questao[]= {"2+2=4","2+2=8","2+5=9","2+3=5"};
		Boolean resposta[] = {true,false,false,true};
		Random gerador = new Random();
		int aux = gerador.nextInt(questao.length);
		circulo.setMensagem(questao[aux]);
		circulo.setResposta(resposta[aux]);
		System.out.println(circulo.getMsg());
		System.out.println(resposta[aux]);
		return circulo;
	}
	
	public Circulo gerarVisual(Circulo oldCirculo){
		Circulo circulo = oldCirculo;
		Random gerador = new Random();
		String cores[] = {"vazio","azul","roxo","verde","vermelho"};
		int aux = gerador.nextInt(cores.length);
		circulo.setTexture(new Texture("images/circulo0"+aux+".png"));
		
		return circulo;
	}
	public boolean acabouJogo(){
		if (hud.getTimer() == 0) {
			return true;
		}else{return false;}
		
	}
	public void Pontuar(int Tipo){
		hud.setScore(Tipo);
	}
	
}
