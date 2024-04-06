import java.util.List;

import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_client.screen_components.Button;
import com.softwarearchitecture.math.Vector2;

public abstract class State {
    protected ScreenManager screenManager;
    protected Vector2 mouse = new Vector2(0, 0);
    protected List<Button> buttons;

    protected State() {
        this.screenManager = ScreenManager.getInstance();
    }

    /**
     * Updates the buttons in the state, and checks if they are clicked.
     * 
     * @param deltaTime: float
     */
    protected void updateButtons(float deltaTime) {
        mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        for (Button button : buttons) {
            button.update(mouse);
        }
    }

    protected abstract void update(float deltaTime);

    protected abstract void handleInput();

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();
}
