package com.jobeslegacy.engine.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.jobeslegacy.engine.ecs.system.GridSystemConfig.GRID_CELL_SIZE_F
import com.jobeslegacy.engine.ecs.system.GridSystemConfig.WIDTH
import com.jobeslegacy.engine.ecs.system.GridSystemConfig.HEIGHT
import com.lehaine.littlekt.extras.grid.entity.GridEntity
import com.lehaine.littlekt.math.castRay
import com.lehaine.littlekt.math.dist
import com.lehaine.littlekt.math.geom.Angle
import com.lehaine.littlekt.math.geom.radians
import com.lehaine.littlekt.math.interpolate
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

/**
 *
 * Taken over and adapted from littlekt-extras
 *
 * TODO check what is static config and
 *      what is dynamic config -> This needs to be serialized for save-game
 *
 * /**
 *  * This component is used to add generic object properties like position and size to an entity.
 *  * The data from this component will be processed e.g. by the KorgeViewSystem in the Fleks ECS.
 *  */
 * @Serializable @SerialName("PositionShape")
 * data class PositionShape(
 *     var initialized: Boolean = false,
 *     var x: Float = 0.0f,
 *     var y: Float = 0.0f,
 *     var width: Float = 0.0f,
 *     var height: Float = 0.0f,
 *     ) : Component<PositionShape>, SerializeBase {
 *     override fun type() = PositionShape
 *     companion object : ComponentType<PositionShape>()
 * }
 *
 */
class GridComponent(
    var cx: Int = 0,
    var cy: Int = 0,
    var xr: Float = 0f,
    var yr: Float = 0f,
    var zr: Float = 0f,
    var anchorX: Float = 0.5f,
    var anchorY: Float = 0.5f,
) : Component<GridComponent> {

    var maxGridMovementPercent: Float = 0.33f

    val innerRadius get() = min(WIDTH, HEIGHT) * 0.5f
    val outerRadius get() = max(WIDTH, HEIGHT) * 0.5f

    private var interpolatePixelPosition: Boolean = false //true

    var interpolationAlpha: Float = 1f

    var lastPx: Float = 0f
    var lastPy: Float = 0f

    var x: Float
        get() {
            return if (interpolatePixelPosition) {
                interpolationAlpha.interpolate(lastPx, attachX)
            } else {
                attachX
            }
        }
        set(value) {
            cx = (value / GRID_CELL_SIZE_F).toInt()
            xr = (value - cx * GRID_CELL_SIZE_F) / GRID_CELL_SIZE_F
            onPositionManuallyChanged()
        }

    var y: Float
        get() {
            return if (interpolatePixelPosition) {
                interpolationAlpha.interpolate(lastPy, attachY)
            } else {
                attachY
            }
        }
        set(value) {
            cy = (value / GRID_CELL_SIZE_F).toInt()
            yr = (value - cy * GRID_CELL_SIZE_F) / GRID_CELL_SIZE_F
            onPositionManuallyChanged()
        }

    var scaleX: Float = 1f
    var scaleY: Float = 1f
    var rotation: Angle = Angle.ZERO

    val attachX get() = (cx + xr) * GRID_CELL_SIZE_F
    val attachY get() = (cy + yr - zr) * GRID_CELL_SIZE_F
    val centerX get() = attachX + (0.5f - anchorX) * WIDTH
    val centerY get() = attachY + (0.5f - anchorY) * HEIGHT
    val top get() = attachY - anchorY * HEIGHT
    val right get() = attachX + (1 - anchorX) * WIDTH
    val bottom get() = attachY + (1 - anchorY) * HEIGHT
    val left get() = attachX - anchorX * WIDTH

    fun castRayTo(tcx: Int, tcy: Int, canRayPass: (Int, Int) -> Boolean) =
        castRay(cx, cy, tcx, tcy, canRayPass)

    fun castRayTo(target: GridEntity, canRayPass: (Int, Int) -> Boolean) =
        castRay(cx, cy, target.cx, target.cy, canRayPass)

    fun toGridPosition(cx: Int, cy: Int, xr: Float = 0.5f, yr: Float = 1f) {
        this.cx = cx
        this.cy = cy
        this.xr = xr
        this.yr = yr
        onPositionManuallyChanged()
    }

    fun dirTo(target: GridComponent) = dirTo(target.centerX)

    fun dirTo(targetX: Float) = if (targetX > centerX) 1 else -1

    fun distGridTo(tcx: Int, tcy: Int, txr: Float = 0.5f, tyr: Float = 0.5f) =
        dist(cx + xr, cy + yr, tcx + txr, tcy + tyr)

    fun distGridTo(target: GridComponent) = distGridTo(target.cx, target.cy, target.xr, target.yr)

    fun distPxTo(x: Float, y: Float) = dist(this.x, this.y, x, y)
    fun distPxTo(target: GridComponent) = distPxTo(target.x, target.y)

    fun angleTo(x: Float, y: Float) = atan2(y - this.y, x - this.x).radians
    fun angleTo(target: GridComponent) = angleTo(target.centerX, target.centerY)

    fun onPositionManuallyChanged() {
        lastPx = attachX
        lastPy = attachY
    }

    override fun toString() : String =
        "GridComponent(cx=$cx, cy=$cy, xr=$xr, yr=$yr, x=$x, y=$y)"

    override fun type(): ComponentType<GridComponent> = GridComponent

    companion object : ComponentType<GridComponent>()
}