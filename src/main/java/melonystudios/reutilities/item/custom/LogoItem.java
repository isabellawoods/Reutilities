package melonystudios.reutilities.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LogoItem extends Item {
    private final int color;

    public LogoItem(int color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    @NotNull
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withColor(this.color);
    }
}
