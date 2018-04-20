package mx.itesm.dragon.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import mx.itesm.dragon.Main;
import mx.itesm.dragon.States.ScreenState;
import mx.itesm.dragon.Utils.BackGround;

public class MenuScreen extends GenericScreen {

    // Escena para el menu.
    private Stage stageMenu;

    private ImageButton imgDragon;
    private ImageButton btnPlay;
    private ImageButton btnAU;
    private ImageButton btnConfig;

    // BackGround.
    private BackGround backGround;
    private Texture textureBackground;

    //
    private Texture textureDragon;
    private Texture textureBtnPlay;
    private Texture textureBtnPressPlay;
    private Texture textureBtnAU;
    private Texture textureBtnPressAU;
    private Texture textureBtnSettings;
    private Texture textureBtnPressSettings;

    private Music musicMenu;
    private Sound soundAU;
    private Sound soundPlay;
    private Sound soundSettings;

    public MenuScreen(Main game) {
        super(game);
    }

    public void show() {
        crearMenu();
    }

    private void crearMenu() {
        // Creación escena menú.
        stageMenu = new Stage(vista);

        // Creacion del backGround.
        textureBackground = assetManager.get("backgrounds/mainMenu.jpg");
        backGround = new BackGround(textureBackground);

        // Creación de los botones del menú.
        textureDragon = assetManager.get("textures/dragonPlay1.png");
        textureBtnPlay = assetManager.get("buttons/play.png");
        textureBtnPressPlay = assetManager.get("buttons/playPressed.png");
        textureBtnAU = assetManager.get("buttons/about.png");
        textureBtnPressAU = assetManager.get("buttons/aboutPressed.png");
        textureBtnSettings = assetManager.get("buttons/settings.png");
        textureBtnPressSettings = assetManager.get("buttons/settingsPressed.png");
        imgDragon = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureDragon)));
        btnPlay = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPlay)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressPlay)));
        btnAU = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnAU)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressAU)));
        btnConfig = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnSettings)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressSettings)));

        // Creacion de la musica de backGround.
        musicMenu = assetManager.get("music/premenu.mp3");
        soundAU = assetManager.get("music/config.wav");
        soundPlay = assetManager.get("music/jugar.wav");
        soundSettings = assetManager.get("music/config.wav");

        // Reproduccion de la musica de backGround
        musicMenu.setVolume(1);
        musicMenu.play();
        musicMenu.setLooping(true);

        // Posición de los botones.
        imgDragon.setPosition((ANCHO / 2) - imgDragon.getWidth() / 2,ALTO * 0.15F);

        btnPlay.setPosition((ANCHO / 2) - btnPlay.getWidth() / 2,imgDragon.getHeight() * 0.53f );

        btnAU.setPosition(ANCHO - btnAU.getWidth()-10,ALTO - btnAU.getHeight()-20);

        btnConfig.setPosition(ANCHO - btnConfig.getWidth()-20,btnConfig.getHeight()/4);

        // Detecta si el usuario hace click en algún actor.
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                soundPlay.play();
                // Cambia de pantalla, solo lo puede hacerlo 'game'.
                game.setScreen(new GenericScreenCargando(game));
            }
        });

        btnAU.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                soundAU.play();
                // Cambia de pantalla, solo lo puede hacerlo 'game'.
                game.setScreen(new LoadingScreen(game, ScreenState.ABOUT));
            }
        });

        btnConfig.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                soundSettings.play();
                game.setScreen(new LoadingScreen(game, ScreenState.SETTINGS));
            }
        });

        // Se agregan elementos al menú.
        stageMenu.addActor(imgDragon);
        stageMenu.addActor(btnPlay);
        stageMenu.addActor(btnAU);
        stageMenu.addActor(btnConfig);

        // Indica quién escucha y atiende eventos.
        Gdx.input.setInputProcessor(stageMenu);
    }

    @Override
    public void render(float delta) {
        // DIBUJAR.
        borrarPantalla();
        batch.begin();
            // Dibujar elementos de la pantalla.
            backGround.render(batch);
        batch.end();
        stageMenu.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stageMenu.dispose();
        batch.dispose();
        assetManager.unload("backgrounds/mainMenu.jpg");
        assetManager.unload("textures/dragonPlay1.png");
        assetManager.unload("buttons/play.png");
        assetManager.unload("buttons/playPressed.png");
        assetManager.unload("buttons/about.png");
        assetManager.unload("buttons/aboutPressed.png");
        assetManager.unload("buttons/settings.png");
        assetManager.unload("buttons/settingsPressed.png");
        assetManager.unload("music/premenu.mp3");
        assetManager.unload("music/jugar.wav");
        assetManager.unload("music/config.wav");
    }
}

