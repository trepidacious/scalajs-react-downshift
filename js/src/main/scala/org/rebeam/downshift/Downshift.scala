
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
    var inputValue: js.Any = js.native
    var highlightedIndex: js.Any = js.native
    var selectedItem: js.Any = js.native
    var id: String = js.native
    // Note actually scalajs.js.Function1[scalajs.js.Function0[Unit], Unit]
    var openMenu: scalajs.js.Function0[Unit] = js.native
    // Note actually scalajs.js.Function1[scalajs.js.Function0[Unit], Unit]
    var closeMenu: scalajs.js.Function0[Unit] = js.native
    var selectItemAtIndex: scalajs.js.Function1[Int, Unit] = js.native
    var setHighlightedIndex: scalajs.js.Function1[Int, Unit] = js.native
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
    * @param openMenu
    *   JS function to open the menu, use in callbacks 
    * @param closeMenu
    *   JS function to close the menu, use in callbacks 
    * @param selectItemAtIndex
    *   JS function to select a specified item in the menu, use in callbacks 
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
    openMenu: scalajs.js.Function0[Unit],
    closeMenu: scalajs.js.Function0[Unit],
    selectItemAtIndex: scalajs.js.Function1[Int, Unit],
    setHighlightedIndex: scalajs.js.Function1[Int, Unit]
  )

  private def optionInt(v: js.Any): Option[Int] = if (v == null) None else Some(v.asInstanceOf[Int])
  private def option[A](v: js.Any): Option[A] = if (v == null) None else Some(v.asInstanceOf[A])

  // Downshift can provide a null inputValue when tabbing away from input
  private def denullString(v: js.Any): String = if (v == null) "" else v.toString

  private def renderStateFrom[A](c: ChildrenFunctionParams): RenderState[A] = 
    RenderState[A](
      c.getToggleButtonProps,
      c.getInputProps,
      (i: ItemData[A]) => c.getItemProps(i.toJS),
      c.getLabelProps,
      c.getMenuProps,
      c.getRootProps,
      c.isOpen,
      denullString(c.inputValue),
      optionInt(c.highlightedIndex),
      option[A](c.selectedItem),
      c.id,
      c.openMenu,
      c.closeMenu,
      c.selectItemAtIndex,
      c.setHighlightedIndex
    )

  @js.native
  trait Props extends js.Object {

    var children: scalajs.js.Function1[ChildrenFunctionParams, japgolly.scalajs.react.raw.React.Element] = js.native
    var itemToString: scalajs.js.Function1[js.Any, String] = js.native

    var selectedItem: js.UndefOr[js.Any] = js.native
    var onChange: js.UndefOr[scalajs.js.Function2[js.Any, ChildrenFunctionParams, Unit]] = js.native

    var inputValue: js.UndefOr[String] = js.native
    var onInputValueChange: js.UndefOr[scalajs.js.Function2[js.Any, ChildrenFunctionParams, Unit]] = js.native

  }

  @JSImport("downshift", JSImport.Default)
  @js.native
  object DownshiftJS extends js.Object

  val jsFnComponent = JsFnComponent[Props, Children.None](DownshiftJS)
  
  /**
    * Create a Downshift component, with selectedItem and inputValue both controlled
    *
    * @param itemToString Render an item to its string representation in the input element
    * @param selectedItem The selected item, to use Downshift as a controlled component
    * @param onChange  Called when selection changes, with new selection and the same params passed to children parameter
    * @param inputValue The value of the input element, to use Downshift as a controlled component
    * @param onInputValueChange Called when input value changes, with new value and the same params passed to children parameter
    *
    * @param children  Named to match Downshift in javascript - this is a function to render the contents
    */
  def apply[A](
    itemToString: A => String,
    selectedItem: Option[A],
    onChange: (Option[A], RenderState[A]) => Callback,
    inputValue: String,
    onInputValueChange: (String, RenderState[A]) => Callback
  )(children: RenderState[A] => VdomElement) = {

    val p = (new js.Object).asInstanceOf[Props]
    
    p.itemToString = (item: js.Any) => if (item == null) "" else itemToString(item.asInstanceOf[A])

    p.selectedItem = selectedItem.getOrElse(null).asInstanceOf[js.Any]
    p.onChange = js.defined((item: js.Any, c: ChildrenFunctionParams) => onChange(option[A](item), renderStateFrom(c)).runNow)

    p.inputValue = inputValue
    p.onInputValueChange = js.defined((inputValue: js.Any, c: ChildrenFunctionParams) => onInputValueChange(denullString(inputValue), renderStateFrom(c)).runNow)

    p.children = (e: ChildrenFunctionParams) => children(renderStateFrom(e)).rawElement

    jsFnComponent(p)
  }

  /**
    * Create a Downshift component, with only selectedItem controlled (normally this is sufficient)
    * Note https://github.com/paypal/downshift/issues/512 - hopefully this will be fixed soon
    *
    * @param itemToString Render an item to its string representation in the input element
    * @param selectedItem The selected item, to use Downshift as a controlled component
    * @param onChange  Called when selection changes, with new selection and the same params passed to children parameter
    *
    * @param children  Named to match Downshift in javascript - this is a function to render the contents
    */
  def apply[A](
    itemToString: A => String,
    selectedItem: Option[A],
    onChange: (Option[A], RenderState[A]) => Callback,
  )(children: RenderState[A] => VdomElement) = {

    val p = (new js.Object).asInstanceOf[Props]
    
    p.itemToString = (item: js.Any) => if (item == null) "" else itemToString(item.asInstanceOf[A])

    p.selectedItem = selectedItem.getOrElse(null).asInstanceOf[js.Any]
    p.onChange = js.defined((item: js.Any, c: ChildrenFunctionParams) => onChange(option[A](item), renderStateFrom(c)).runNow)

    p.children = (e: ChildrenFunctionParams) => children(renderStateFrom(e)).rawElement

    jsFnComponent(p)
  }

  /**
    * Create a Downshift component, uncontrolled
    *
    * @param itemToString Render an item to its string representation in the input element
    * @param onChange  Called when selection changes, with new selection and the same params passed to children parameter
    *
    * @param children  Named to match Downshift in javascript - this is a function to render the contents
    */
  def apply[A](
    itemToString: A => String,
    onChange: (Option[A], RenderState[A]) => Callback,
  )(children: RenderState[A] => VdomElement) = {

    val p = (new js.Object).asInstanceOf[Props]
    
    p.itemToString = (item: js.Any) => if (item == null) "" else itemToString(item.asInstanceOf[A])

    p.onChange = js.defined((item: js.Any, c: ChildrenFunctionParams) => onChange(option[A](item), renderStateFrom(c)).runNow)

    p.children = (e: ChildrenFunctionParams) => children(renderStateFrom(e)).rawElement

    jsFnComponent(p)
  }

  /**
    * Create a Downshift component, with inputValue controlled
    *
    * @param itemToString Render an item to its string representation in the input element
    * @param onChange  Called when selection changes, with new selection and the same params passed to children parameter
    * @param inputValue The value of the input element, to use Downshift as a controlled component
    * @param onInputValueChange Called when input value changes, with new value and the same params passed to children parameter
    *
    * @param children  Named to match Downshift in javascript - this is a function to render the contents
    */
  def apply[A](
    itemToString: A => String,
    onChange: (Option[A], RenderState[A]) => Callback,
    inputValue: String,
    onInputValueChange: (String, RenderState[A]) => Callback
  )(children: RenderState[A] => VdomElement) = {

    val p = (new js.Object).asInstanceOf[Props]
    
    p.itemToString = (item: js.Any) => if (item == null) "" else itemToString(item.asInstanceOf[A])

    p.onChange = js.defined((item: js.Any, c: ChildrenFunctionParams) => onChange(option[A](item), renderStateFrom(c)).runNow)

    p.inputValue = inputValue
    p.onInputValueChange = js.defined((inputValue: js.Any, c: ChildrenFunctionParams) => onInputValueChange(denullString(inputValue), renderStateFrom(c)).runNow)

    p.children = (e: ChildrenFunctionParams) => children(renderStateFrom(e)).rawElement

    jsFnComponent(p)
  }
}
        