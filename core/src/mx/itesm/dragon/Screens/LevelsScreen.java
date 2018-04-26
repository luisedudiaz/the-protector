package mx.itesm.dragon.Screens;

import com.badlogic.gdx.Gdx;
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

public class LevelsScreen extends GenericScreen{


    private ScreenState screenState;

    private Texture textureBackground;
    private Texture textureBtnLvl1;
    private Texture textureBtnPressLvl1;
    private Texture textureBtnLvl2;
    private Texture textureBtnPressLvl2;
    private Texture textureBtnLvl3;
    private Texture textureBtnPressLvl3;


    private ImageButton btnLvl1;
    private ImageButton btnLvl2;
    private ImageButton btnLvl3;

    private BackGround backGround;

    private Stage stageLevelsSreen;

    public LevelsScreen(Main game, ScreenState screenState) {
        super(game);
        this.screenState = screenState;
    }

    @Override
    public void show() {
        borrarPantalla();
        loadResources();
    }

    private void loadResources() {
        stageLevelsSreen = new Stage(vista);

        textureBackground = assetManager.get("backgrounds/loading.jpg");
        backGround = new BackGround(textureBackground);

        textureBtnLvl1 = assetManager.get("buttons/resume.png");
        textureBtnPressLvl1 = assetManager.get("buttons/resumePressed.png");
        textureBtnLvl2 = assetManager.get("buttons/resume.png");
        textureBtnPressLvl2 = assetManager.get("buttons/resumePressed.png");
        textureBtnLvl3 = assetManager.get("buttons/resume.png");
        textureBtnPressLvl3 = assetManager.get("buttons/resumePressed.png");

        btnLvl1 = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnLvl1)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressLvl1)));

        btnLvl2 = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnLvl2)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressLvl2)));

        btnLvl3 = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnLvl3)),
                new TextureRegionDrawable(
                        new TextureRegion(
                                textureBtnPressLvl3)));

        btnLvl1.setPosition(0,0);
        btnLvl2.setPosition(btnLvl1.getWidth(),0);
        btnLvl3.setPosition(btnLvl1.getWidth() + btnLvl2.getWidth(), 0);

        btnLvl1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game, ScreenState.LVL_ONE));
            }
        });

        btnLvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game, ScreenState.LVL_TWO));
            }
        });

        btnLvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game, ScreenState.LVL_THREE));
            }
        });

        stageLevelsSreen.addActor(btnLvl1);
        stageLevelsSreen.addActor(btnLvl2);
        stageLevelsSreen.addActor(btnLvl3);

        Gdx.input.setInputProcessor(stageLevelsSreen);
    }

    @Override
    public void render(float delta) {
        batch.begin();
            backGround.render(batch);
        batch.end();
        stageLevelsSreen.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        assetManager.unload("backgrounds/loading.jpg");
        assetManager.unload("buttons/resume.png");
        assetManager.unload("buttons/resumePressed.png");
    }
}