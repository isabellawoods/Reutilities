package melonystudios.behaviorapi.custom;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;

import java.util.Optional;

public class RunFunctionBehavior extends ItemBehavior {
    public static final MapCodec<RunFunctionBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RunFunction.CODEC.forGetter(RunFunctionBehavior::runFunction),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, RunFunctionBehavior::new));
    private static final Logger LOGGER = LogUtils.getLogger();
    private final RunFunction runFunction;

    public RunFunctionBehavior(RunFunction runFunction, GlobalSettings settings) {
        super(runFunction, settings);
        this.runFunction = runFunction;
    }

    public RunFunctionBehavior(RunFunction runFunction) {
        this(runFunction, GlobalSettings.defaults());
    }

    public RunFunctionBehavior() {
        this(new RunFunction(ResourceLocation.withDefaultNamespace("")));
    }

    public RunFunction runFunction() {
        return this.runFunction;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level level, LivingEntity livEntity) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        MinecraftServer server = serverLevel.getServer();
        ServerFunctionManager functionManager = server.getFunctions();
        Optional<CommandFunction<CommandSourceStack>> function = functionManager.get(this.runFunction().function());

        if (function.isPresent()) {
            CommandSourceStack sourceStack = server.createCommandSourceStack()
                    .withPermission(2)
                    .withSuppressedOutput()
                    .withEntity(livEntity)
                    .withLevel(serverLevel)
                    .withPosition(livEntity.position())
                    .withRotation(livEntity.getRotationVector());
            functionManager.execute(function.get(), sourceStack);
        } else {
            LOGGER.error(
                    MarkerFactory.getMarker(livEntity.getDisplayName().getString() + "/" + stack.getDescriptionId()),
                    "'run_function' item behavior running at [{}, {}, {}] failed due to a non-existent function: {}",
                    livEntity.blockPosition().getX(),
                    livEntity.blockPosition().getY(),
                    livEntity.blockPosition().getZ(),
                    this.runFunction().function()
            );
        }
    }

    public record RunFunction(ResourceLocation function) implements IndividualSettings<RunFunction> {
        public static final MapCodec<RunFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("function").forGetter(RunFunction::function)
        ).apply(instance, RunFunction::new));

        @Override
        public MapCodec<RunFunction> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RunFunction> streamCodec() {
            return StreamCodec.composite(ResourceLocation.STREAM_CODEC, RunFunction::function, RunFunction::new);
        }
    }
}
