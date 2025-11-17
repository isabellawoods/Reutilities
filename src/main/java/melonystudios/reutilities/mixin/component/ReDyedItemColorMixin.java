package melonystudios.reutilities.mixin.component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.DyedItemColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Locale;
import java.util.function.Consumer;

@Mixin(DyedItemColor.class)
public abstract class ReDyedItemColorMixin {
    @Shadow
    public abstract int rgb();

    @Redirect(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0))
    public <T> void addColoredTooltip(Consumer<Component> adder, T t) {
        adder.accept(Component.translatable("item.color", Component.literal(String.format(Locale.ROOT, "#%06X", this.rgb())).withColor(this.rgb())).withStyle(ChatFormatting.GRAY));
    }
}
