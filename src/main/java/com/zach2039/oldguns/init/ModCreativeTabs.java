package com.zach2039.oldguns.init;

import com.zach2039.oldguns.OldGuns;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Registers {@link CreativeModeTab}s.
 *
 * @author Zach2039
 */
@Mod.EventBusSubscriber(modid = OldGuns.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OldGuns.MODID);

    private static boolean isInitialized = false;

    public static final DeferredHolder<CreativeModeTab, ? extends CreativeModeTab> OLD_GUNS_CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("tab", () -> {
        final Supplier<ItemStack> firearm = () -> new ItemStack(ModItems.FLINTLOCK_PISTOL.get());

        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.oldguns"))
                .icon(firearm)
                .displayItems((parameters, output) -> {
                    output.accept(firearm.get());
                    add(output, ModBlocks.orderedItems());
                    add(output, ModItems.orderedItems());
                    add(output, ModFluids.orderedItems());
                })
                .build();
    });

    /**
     * Registers the {@link DeferredRegister} instances with the mod event bus.
     * <p>
     * This should be called during mod construction.
     *
     * @param modEventBus The mod event bus
     */
    public static void initialize(final IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Already initialised");
        }

        CREATIVE_MODE_TABS.register(modEventBus);

        isInitialized = true;
    }

    private static void add(final CreativeModeTab.Output output, final Collection<DeferredHolder<Item, ? extends Item>> items) {
        items.stream()
                .map(DeferredHolder::get)
                .forEach(output::accept);
    }

    @Mod.EventBusSubscriber(modid = OldGuns.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void buildContents(final BuildCreativeModeTabContentsEvent event) {
            if (event.getTab() != OLD_GUNS_CREATIVE_MODE_TAB.get()) {
                return;
            }
        }
    }
}