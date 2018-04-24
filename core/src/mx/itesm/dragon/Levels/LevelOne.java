package mx.itesm.dragon.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Random;

import mx.itesm.dragon.Main;
import mx.itesm.dragon.Objects.Boss;
import mx.itesm.dragon.Objects.Enemy;
import mx.itesm.dragon.Objects.Fire;
import mx.itesm.dragon.Objects.LifeCharacter;
import mx.itesm.dragon.States.GameState;
import mx.itesm.dragon.States.ScreenState;
import mx.itesm.dragon.Utils.AnimatedImage;
import mx.itesm.dragon.Utils.BackGround;

public class LevelOne extends GenericLevel {

    private static final float ALTO_MAPA = 2560;

    private float timerProyectil;
    private float timerFlecha;
    private float timerJefeFinal;

    // determinan el movimiemto del jefe final
    private boolean jefePos = false;
    private int direccion = 1;

    private float xDragon;
    private float yDragon;

    private Random random;

    private Texture proyectil;
    private Texture proyectilJefeFinal;
    private Texture flecha;


    private ArrayList<Fire> listaProyectil;
    private ArrayList<Fire> listaProyectilJefe;
    private ArrayList<Enemy> listaFlechas;
    private ArrayList<LifeCharacter> listaVidas;

    private BackGround backGround;

    private Boss framesJefeFinal;

    private ScreenState screenState;

    // Marcador.
    private int puntosJugador = 0;
    private float timerVida;
    private float timerProyectilJefeFinal;


    public LevelOne(Main game, ScreenState lvlOne) {
        super(game);
        screenState = lvlOne;
    }

    @Override
    public void show() {
        borrarPantalla();
        loadResources();
        create();
        initialization();
    }

    private void create() {
        // INICIALIZACIÓN DE COMPONENTES.
        initJuego();
        Gdx.input.setInputProcessor(multiplexer);
    }


    private void initJuego() {
        stageJuego = new Stage(vista);
        timerProyectil = 0;
        timerFlecha = 0;
        timerJefeFinal = 0;
        timerProyectilJefeFinal = 0;

        listaProyectil = new ArrayList<Fire>();
        listaFlechas = new ArrayList<Enemy>();
        listaVidas = new ArrayList<LifeCharacter>();
        listaProyectilJefe = new ArrayList<Fire>();

        random = new Random();

        // Objeto que dibuja al text

        backGround = new BackGround(new Texture("backgrounds/level1.png"));

        framesJefeFinal = new Boss("frames/finalBoss1.png");
        boss = new AnimatedImage(framesJefeFinal.animacion());


        proyectil = new Texture("textures/fireBall.png");
        proyectilJefeFinal = new Texture("textures/rock.png");
        flecha = new Texture("textures/arrow.png");
        lifeCharacter = new LifeCharacter(texturePotion);

        boss.setPosition(0 - boss.getWidth(), ALTO);
        stageJuego.addActor(boss);
        // Se anexan las Escenas al Multiplexor.
    }

    @Override
    public void render(float delta) {
        switch (gameState) {
            case JUGANDO:
                actualizarObjetos(delta);
                stagePausa.unfocusAll();
                batch.begin();

                puntosJugador += 10; // incrementa los puntos del jugador conforme pasa el tiempo;
                backGround.render(batch);
                //Marcador
                text.mostrarMensaje(batch,letras,ANCHO - ANCHO/8, ALTO);
                puntos.mostrarMensaje(batch, Integer.toString(puntosJugador), ANCHO - ANCHO/8, ALTO-50);

                for (Fire p: listaProyectil) {
                    //fuego.play();
                    p.render(batch);
                }
                for (Fire pjf: listaProyectilJefe) {
                    pjf.render(batch);
                }
                for (LifeCharacter v : listaVidas){
                    v.render(batch);
                }

                for (Enemy e: listaFlechas) {
                    //flecha_s.play();
                    e.render(batch);
                }
                batch.end();
                if (btnPausa.isPressed()) {
                    xDragon = dragon.getX();
                    yDragon = dragon.getY();
                    dragon.setPosition(-1000, -1000);
                    pause.play();
                    gameState = GameState.PAUSA;
                }
                stageJuego.draw();
                if(lifeCharacter.getVidas() == 0){
                    gameState = GameState.PERDER;
                }

                if(framesJefeFinal.getVida() == 0){
                    gameState = GameState.GANAR;
                }
                break;
            case PAUSA:
                batch.begin();
                backGroundPausa.render(batch);
                batch.end();
                if (btnReanudar.isPressed()) {
                    resume.play();
                    dragon.setPosition(xDragon, yDragon);
                    gameState = GameState.JUGANDO;
                }
                stagePausa.draw();
                break;
            case PERDER:
                batch.begin();
                backGroundPerder.render(batch);
                text.mostrarMensaje(batch,letras,ANCHO / 2, ALTO - ALTO / 4-50);
                puntos.mostrarMensaje(batch, Integer.toString(puntosJugador), ANCHO / 2, ALTO - ALTO /4 - 100);
                batch.end();

                stagePerder.draw();
                break;
            case GANAR:
                batch.begin();
                backGroundGanar.render(batch);
                text.mostrarMensaje(batch,letras,ANCHO / 2, ALTO - ALTO / 4 - 50);
                puntos.mostrarMensaje(batch, Integer.toString(puntosJugador), ANCHO / 2, ALTO - ALTO / 4 - 100);
                batch.end();
                stageGanar.draw();
                break;
        }
        // Gdx.app.log("render", "fps="+Gdx.graphics.getFramesPerSecond());
    }

    private void actualizarObjetos(float delta) {
        actualizarFondo(delta * 5);
        actualizarProyectiles(delta);
        actualizarEnemigos(delta);
        actualizarColisiones(delta);
        actualizarPersonaje(delta);
        actualizarJefeFinal(delta);
        actualizarCamara();
        actualizarVida(delta);
    }

    private void actualizarCamara() {
        // Depende de la posición del personaje. Siempre sigue al personaje
        float y = dragon.getImageY();
        // Primera mitad de la pantalla.
        if (y < ALTO/2 ) {
            camara.position.set(ANCHO / 2, ALTO / 2, 0);
        } else if (y > ALTO_MAPA - ANCHO / 2) {   // Última mitad de la pantalla
            camara.position.set(camara.position.x,ALTO_MAPA - ANCHO/2,0);
        }
        camara.update();
        moverCamara();
    }

    private void actualizarPersonaje(float delta) {
        dragon.act(delta);
    }

    private void actualizarJefeFinal(float delta) {
        timerJefeFinal += delta;
        boss.act(delta);
        if (timerJefeFinal >= 30){
            if (jefePos) {
                boss.setPosition(boss.getX() + (3 * direccion), boss.getY());

                if (boss.getX() + boss.getWidth() >= ANCHO) {
                    direccion = -1;
                }
                if (boss.getX() <= 0) {
                    direccion = 1;
                }
            }
            else {
                if (boss.getX() >= ANCHO / 2 - boss.getImageWidth() / 2) {
                    //jefeFinal.setPosition(jefeFinal.getX(), jefeFinal.getY());
                    jefePos = true;
                } else {
                    boss.setPosition(boss.getX() + 3, boss.getY() - 3);
                }
            }
        }


    }

    private void actualizarFondo(float delta) {
        backGround.mover(delta);
    }

    private void actualizarColisiones(float delta) {
        for (int i = 0; i < listaProyectil.size(); i++) {
            Fire proyectil = listaProyectil.get(i);
            Rectangle rectProyectil = proyectil.getSprite().getBoundingRectangle();
            Rectangle rectJefeFinal = new Rectangle(boss.getX(), boss.getY(), boss.getImageWidth(), boss.getImageHeight());
            if (rectJefeFinal.overlaps(rectProyectil)) {
                collision.play();
                listaProyectil.remove(i);
                framesJefeFinal.setVida(framesJefeFinal.getVida() - 1);
            }
         }

        for (int i = 0; i < listaVidas.size(); i++) {
            LifeCharacter pocima = listaVidas.get(i);
            Rectangle rectDragon = new Rectangle(dragon.getX() + 151,dragon.getY(),151,dragon.getHeight() / 2);
            Rectangle rectPocima = pocima.getSprite().getBoundingRectangle();
            if (rectDragon.overlaps(rectPocima)) {
                listaVidas.remove(pocima);

                switch (lifeCharacter.getVidas()){
                    case 1:
                        lifeCharacter.setVidas(lifeCharacter.getVidas()+1);
                        v2.setVisible(true);
                        break;
                    case 2:
                        lifeCharacter.setVidas(lifeCharacter.getVidas()+1);
                        v3.setVisible(true);
                        break;
                    case 3:
                        lifeCharacter.setVidas(lifeCharacter.getVidas()+1);
                        v4.setVisible(true);
                        break;
                    default:
                        break;
                }
            }

        }
        for (int i = 0; i < listaProyectil.size(); i++) {
            Fire proyectil = listaProyectil.get(i);
            for (int j = 0; j < listaProyectilJefe.size(); j++) {
                Fire proyectilJefe = listaProyectilJefe.get(j);
                Rectangle rectProyectil = proyectil.getSprite().getBoundingRectangle();
                Rectangle rectProyectilJefe = proyectilJefe.getSprite().getBoundingRectangle();
                if (rectProyectil.overlaps(rectProyectilJefe)) {
                    collision.play();
                    listaProyectil.remove(proyectil);
                }
            }
        }

        for (int i = 0; i < listaProyectil.size(); i++) {
            Fire proyectil = listaProyectil.get(i);
            for (int j = 0; j < listaFlechas.size(); j++) {
                Enemy flechas = listaFlechas.get(j);
                Rectangle rectProyectil = proyectil.getSprite().getBoundingRectangle();
                Rectangle rectFlechas = flechas.getSprite().getBoundingRectangle();
                if (rectProyectil.overlaps(rectFlechas)) {
                    collision.play();
                    listaProyectil.remove(proyectil);
                    listaFlechas.remove(flechas);
                    break;
                }
            }
        }
        for (int i = 0; i < listaFlechas.size(); i++) {
            Enemy flechas = listaFlechas.get(i);
            Rectangle rectDragon = new Rectangle(dragon.getX() + 151,dragon.getY(),151,dragon.getHeight() / 2);
            Rectangle rectJefeFinal = new Rectangle(boss.getX(), boss.getY(), boss.getImageWidth(), boss.getImageHeight());
            Rectangle rectFlechas = flechas.getSprite().getBoundingRectangle();
            if (rectDragon.overlaps(rectFlechas)) {
                impact.play();
                switch (lifeCharacter.getVidas()) {
                    case 1:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v1.setVisible(false);
                        break;
                    case 2:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v2.setVisible(false);
                        break;
                    case 3:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v3.setVisible(false);
                        break;
                    case 4:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v4.setVisible(false);
                        break;
                }
                listaFlechas.remove(i);
                break;
            }
        }
        for (int i = 0; i < listaProyectilJefe.size(); i++) {
            Fire proyectilJefe = listaProyectilJefe.get(i);
            Rectangle rectDragon = new Rectangle(dragon.getX() + 151,dragon.getY(),151,dragon.getHeight() / 2);
            Rectangle rectJefeFinal = proyectilJefe.getSprite().getBoundingRectangle();
            if (rectDragon.overlaps(rectJefeFinal)) {
                impact.play();
                switch (lifeCharacter.getVidas()) {
                    case 1:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v1.setVisible(false);
                        break;
                    case 2:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v2.setVisible(false);
                        break;
                    case 3:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v3.setVisible(false);
                        break;
                    case 4:
                        lifeCharacter.setVidas(lifeCharacter.getVidas() - 1);
                        v4.setVisible(false);
                        break;
                }
                listaProyectilJefe.remove(i);
                break;
            }
        }
    }

    private void actualizarEnemigos(float delta) {
        timerFlecha += delta;
        int randomX = random.nextInt((int) ANCHO - flecha.getWidth());
        if (timerFlecha >= .75f) {
            listaFlechas.add(new Enemy(flecha, randomX, ALTO));
            timerFlecha = 0;
        }
        for (int i = 0; i < listaFlechas.size(); i++) {
            listaFlechas.get(i).mover();
        }
        for (int i = 0; i < listaFlechas.size(); i++) {
            if (listaFlechas.get(i).getSprite().getHeight() <= 0) {
                listaFlechas.remove(i);
            }
        }
    }

    private void actualizarProyectiles(float delta) {
        timerProyectil += delta;
        timerProyectilJefeFinal += delta;
        if (timerProyectil >= .350f){
            listaProyectil.add(new Fire(proyectil, dragon.getX() + dragon.getWidth() / 2 - proyectil.getWidth() / 2, dragon.getY() + dragon.getHeight()));
            timerProyectil = 0;
        }
        for (int i = 0; i < listaProyectil.size(); i++) {
            listaProyectil.get(i).mover();
        }
        for (int i = 0; i < listaProyectil.size(); i++) {
            if (listaProyectil.get(i).getSprite().getY() >= ALTO) {
                listaProyectil.remove(i);
            }
        }
        if (timerProyectilJefeFinal >= 1f) {
            listaProyectilJefe.add(new Fire(proyectilJefeFinal,boss.getX() + boss.getWidth() / 2 - proyectilJefeFinal.getWidth() / 2, boss.getY()));
            timerProyectilJefeFinal = 0;
        }
        for (int i = 0; i < listaProyectilJefe.size(); i++) {
            listaProyectilJefe.get(i).moverAbajo();
        }
        for (int i = 0; i < listaProyectilJefe.size(); i++) {
            if (listaProyectilJefe.get(i).getSprite().getY() <= 0) {
                listaProyectilJefe.remove(i);
            }
        }
    }

    private void actualizarVida(float delta){
        timerVida += delta;
        int randomX = random.nextInt((int) ANCHO -texturePotion.getWidth());
        if (timerVida >= 5){
            listaVidas.add(new LifeCharacter(texturePotion, randomX, ALTO));
            timerVida = 0;
        }
        for (int i = 0; i < listaVidas.size(); i++) {
            listaVidas.get(i).mover();
        }
        for (int i = 0; i < listaVidas.size(); i++) {
            if (listaVidas.get(i).getSprite().getY() >= ALTO) {
                listaVidas.remove(i);
            }
        }
    }



    @Override
    public void pause() {
        gameState = GameState.PAUSA;
    }

    @Override
    public void resume() {
        gameState = GameState.PAUSA;
    }

    @Override
    public void dispose() {
        stageJuego.dispose();
        stagePausa.dispose();
        stagePerder.dispose();
        stageGanar.dispose();
        proyectil.dispose();
        flecha.dispose();
        assetManager.unload("textures/healthBar.png");
        assetManager.unload("textures/heart.png");
        assetManager.unload("buttons/pause.png");
        assetManager.unload("frames/dragon.png");
        assetManager.unload("textures/potion.png");
        assetManager.unload("backgrounds/pause.png");
        assetManager.unload("buttons/resume.png");
        assetManager.unload("buttons/resumePressed.png");
        assetManager.unload("buttons/music.png");
        assetManager.unload("buttons/musicPressed.png");
        assetManager.unload("buttons/sfx.png");
        assetManager.unload("buttons/sfxPressed.png");
        assetManager.unload("buttons/mainMenu.png");
        assetManager.unload("buttons/mainMenuPressed.png");
        assetManager.unload("backgrounds/win.png");
        assetManager.unload("backgrounds/gameOver.png");
        assetManager.unload("buttons/reset.png");
        assetManager.unload("buttons/resetPressed.png");
        assetManager.unload("music/Hyrule Field - The Legend of Zelda Twilight Princess.mp3");
        assetManager.unload("music/flecha.wav");
        assetManager.unload("music/colision.wav");
        assetManager.unload("music/fuego.wav");
        assetManager.unload("music/pausa.wav");
        assetManager.unload("music/reanudar.wav");
        assetManager.unload("music/impacto.wav");
    }
}