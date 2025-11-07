package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public class ReBlockTagsProvider extends BlockTagsProvider {
    public ReBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(output, registries, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Block Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {}
}
