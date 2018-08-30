
package org.rebeam.downshift

import japgolly.scalajs.react._
import scalajs.js
import scalajs.js.annotation.JSImport

import japgolly.scalajs.react.vdom.html_<^._

object Downshift {

  @js.native
  trait GetItemPropsParams extends js.Object {
    var item: js.Any = js.native
    var index: js.UndefOr[Int] = js.native
    var disabled: js.UndefOr[Boolean] = js.native
  }

  @js.native
  trait ChildrenFunctionParams extends js.Object {
    var getToggleButtonProps: scalajs.js.Function1[js.Object, js.Object] = js.native
    var getInputProps: scalajs.js.Function1[js.Object, js.Object] = js.native
    var getItemProps: scalajs.js.Function1[GetItemPropsParams, js.Object] = js.native
    var getLabelProps: scalajs.js.Function1[js.Object, js.Object] = js.native
    var getMenuProps: scalajs.js.Function1[js.Object, js.Object] = js.native
    var getRootProps: scalajs.js.Function1[js.Object, js.Object] = js.native
    var isOpen: Boolean = js.native
    var inputValue: String = js.native
    var highlightedIndex: js.Any = js.native
    var selectedItem: js.Any = js.native
    var id: String = js.native
  }

  /**
    * The data associated with one item in the menu - provided to Downshift to generate
    * props for the rendered element for the item
    * @param item  The item itself
    * @param index  The index of the item within the list
    * @param disabled  True if the selection is disabled
    */
  case class ItemData[A](item: A, index: Int, disabled: Boolean) {
    def toJS: GetItemPropsParams = {
      val p = (new js.Object).asInstanceOf[GetItemPropsParams]
      p.item = item.asInstanceOf[js.Any]
      p.index = index
      p.disabled = disabled
      p
    }
  }

  /**
    * Downshift provides this data to the "children" render function to allow it to
    * style the rendered elements with props required by Downshift.
    * @param getToggleButtonProps 
    *   Function accepting your desired props for any menu toggle button element rendered,
    *   and returning all props that should be applied.
    *   Note each "get" function below works the same way, but applies to a different 
    *   rendered element.
    * @param getInputProps 
    *   As per getToggleButtonProps but for input element
    * @param getItemProps 
    *   As per getToggleButtonProps but for menu item elements
    * @param getLabelProps 
    *   As per getToggleButtonProps but for label element. See docs for required
    *   and optional properties to pass in
    * @param getMenuProps 
    *   As per getToggleButtonProps but for ul element or root of your menu
    * @param getRootProps 
    *   As per getToggleButtonProps but for root element - optional, not needed if
    *   root element is a div.
    * @param isOpen 
    *   True if menu is open
    * @param inputValue 
    *   Current value of the getInputProps input
    * @param highlightedIndex 
    *   Index of the currently highlighted item, if any
    * @param selectedItem 
    *   The currently selected item input
    * @param id 
    *   The id passed to Downshift component as a prop
    */
  case class RenderState[A] (
    getToggleButtonProps: js.Object => js.Object,
    getInputProps: js.Object => js.Object,
    getItemProps: ItemData[A] => js.Object,
    getLabelProps: js.Object => js.Object,
    getMenuProps: js.Object => js.Object,
    getRootProps: js.Object => js.Object,
    isOpen: Boolean,
    inputValue: String,
    highlightedIndex: Option[Int],
    selectedItem: Option[A],
    id: String,
  )

  private def optionInt(v: js.Any): Option[Int] = if (v == null) None else Some(v.asInstanceOf[Int])
  private def option[A](v: js.Any): Option[A] = if (v == null) None else Some(v.asInstanceOf[A])

  private def renderStateFrom[A](c: ChildrenFunctionParams): RenderState[A] = 
    RenderState[A](
      c.getToggleButtonProps,
      c.getInputProps,
      (i: ItemData[A]) => c.getItemProps(i.toJS),
      c.getLabelProps,
      c.getMenuProps,
      c.getRootProps,
      c.isOpen,
      c.inputValue,
      optionInt(c.highlightedIndex),
      option[A](c.selectedItem),
      c.id)

  @js.native
  trait Props extends js.Object {

    // var variant: js.UndefOr[String] = js.native
    var children: scalajs.js.Function1[ChildrenFunctionParams, japgolly.scalajs.react.raw.React.Element] = js.native

    /**
     * Called when the user selects an item and the selected item has changed. 
     * Called with the item that was selected and the new state of downshift. (see onStateChange for more info on stateAndHelpers).
     * First param: The item that was just selected
     * Second param: Params object as provided to children
     */
    var onChange: scalajs.js.Function2[js.Any, ChildrenFunctionParams, Unit] = js.native

    /**
     * Used to determine the string value for the selected item (which is used to compute the inputValue).
     */
    var itemToString: js.UndefOr[scalajs.js.Function1[js.Any, String]] = js.native

    /**
     * The selected item, to use Downshift as a controlled component
     */
    var selectedItem: js.Any = js.native
  }

  @JSImport("downshift", JSImport.Default)
  @js.native
  object DownshiftJS extends js.Object

  val jsFnComponent = JsFnComponent[Props, Children.None](DownshiftJS)
  
  /**
   * @param onChange  Called when selection changes, with new selection and the same params passed to children parameter
   * @param children  Named to match Downshift in javascript - this is a function to render the contents
   */
  def apply[A](
    onChange: (Option[A], RenderState[A]) => Callback,
    selectedItem: Option[A]
  )(children: RenderState[A] => VdomElement) = {

    val p = (new js.Object).asInstanceOf[Props]
    
    p.children = (e: ChildrenFunctionParams) => children(renderStateFrom(e)).rawElement
    p.selectedItem = selectedItem.getOrElse(null).asInstanceOf[js.Any]
    p.onChange = (item: js.Any, c: ChildrenFunctionParams) => onChange(option[A](item), renderStateFrom(c)).runNow

    jsFnComponent(p)
  }

}
        