package kvs.core.util;

import java.io.Serializable;
import java.util.LinkedList;
/**
 * 木構造を表すクラスです。
 * 根の位置にinsert()することで複数の根を追加することも可能です。
 * @author eguchi
 */
public class Tree<T> implements Serializable{

    private static final long serialVersionUID = 2039978796692292557L;
    protected Node<T> m_head;

    /**
     * デフォルトのTreeを返します。
     */
    public Tree() {
        this.initialize_head();
    }

    /**
     * 指定された要素を先頭に持つTreeを返します。
     * @param t
     */
    public Tree( T t ) {
        this.initialize_head();
        this.set_head( t );
    }

    /**
     * この木構造の前順走査を行う反復子を返します。
     * @return 反復子
     */
    public final PreOrderIterator<T> begin() {
        return (new PreOrderIterator<T>( m_head.next_sibling ));
    }

    /**
     * この木構造の前順走査を行う反復子の終端を返します。
     * @return 反復子の終端
     */
    public final PreOrderIterator<T> end() {
        return (new PreOrderIterator<T>( m_head ));
    }

    /**
     * この木構造の後順走査を行う反復子を返します。
     * @return 反復子
     */
    public final PostOrderIterator<T> begin_post() {
        Node<T> tmp = m_head.next_sibling;

        if (tmp != m_head) {
            while (tmp.first_child != null) {
                tmp = tmp.first_child;
            }
        }

        return (new PostOrderIterator<T>( tmp ));
    }

    /**
     * この木構造の後順走査を行う反復子の終端を返します。
     * @return 反復子の終端
     */
    public final PostOrderIterator<T> end_post() {
        return (new PostOrderIterator<T>( m_head ));
    }

    /**
     * 指定された反復子の現在の位置を親とする兄弟ノードを走査する反復子を返します。
     * 指定された反復子が子を持たない場合は終端を返します。
     * @param pos 走査する反復子の親
     * @return 反復子
     */
    public final SiblingIterator<T> begin( final Iterator<T> pos ) {
        if (pos.node().first_child == null) {
            return (this.end( pos ));
        }

        return (new SiblingIterator<T>( pos.node().first_child ));
    }

    /**
     * 指定された反復子の現在の位置を親とする兄弟ノードを走査する反復子の終端を返します。
     * @param pos 走査する反復子の親
     * @return 反復子の終端
     */
    public SiblingIterator<T> end( final Iterator<T> pos ) {
        SiblingIterator<T> ret = new SiblingIterator<T>();
        ret.m_parent = pos.node();

        return (ret);
    }

    /**
     * この木構造の幅優先探索を行う反復子を返します。
     * @return 反復子
     */
    public final BreadthFirstIterator<T> begin_breadth() {
        return (new BreadthFirstIterator<T>( m_head.next_sibling ));
    }

    /**
     * この木構造の幅優先探索を行う反復子の終端を返します。
     * @return 反復子の終端
     */
    public final BreadthFirstIterator<T> end_breadth() {

        return (new BreadthFirstIterator<T>());

    }

    public LeafIterator<T> begin_leaf() {
        Node<T> tmp = m_head.next_sibling;

        if (tmp != m_head) {
            while (tmp.first_child != null) {
                tmp = tmp.first_child;
            }
        }

        return (new LeafIterator<T>( tmp ));
    }

    public final LeafIterator<T> end_leaf() {
        return (new LeafIterator<T>( m_head ));
    }

    /**
     * この木構造のすべての要素を削除します。
     */
    public void clear() {
        if (m_head != null) {
            while (m_head.next_sibling != m_head) {
                this.erase( new PreOrderIterator<T>( m_head.next_sibling ) );
            }
        }
    }

    /**
     * 指定された反復子の指すノードとそのすべての子を削除します。
     * @param it 削除するノードを指す反復子
     * @return 
     */
    public Iterator<T> erase( Iterator<T> it ) {
        Node<T> cur = it.node();

        // KVS_ASSERT( cur != m_head );

        Iterator<T> ret = it;
        ret.skip_children();
        ret.next();

        this.erase_children( it );
        if (cur.prev_sibling == null) {
            cur.parent.first_child = cur.next_sibling;
        } else {
            cur.prev_sibling.next_sibling = cur.next_sibling;
        }

        if (cur.next_sibling == null) {
            cur.parent.last_child = cur.prev_sibling;
        } else {
            cur.next_sibling.prev_sibling = cur.prev_sibling;
        }

        // this->destructor( &cur->data );
        // m_allocator.deallocate( cur, 1 );

        return (ret);
    }

    /**
     * 指定された反復子の指すノードのすべての子を削除します。
     * @param it 削除するノードの親を指す反復子
     */
    public void erase_children( final Iterator<T> it ) {
        Node<T> cur = it.node().first_child;
        Node<T> prev = null;

        while (cur != null) {
            prev = cur;
            cur = cur.next_sibling;

            this.erase_children( new PreOrderIterator<T>( prev ) );
            // this.destructor( prev.data );
            // m_allocator.deallocate( prev, 1 );
        }

        it.node().first_child = null;
        it.node().last_child = null;
    }

    /**
     * 指定された反復子の指すノードに子を追加します。
     * @param position 親となるノードを指す反復子
     * @return 追加されたノードを指す反復子
     */
    public Iterator<T> append_child( Iterator<T> position ) {
        // KVS_ASSERT( position.node() != m_head );

        Node<T> tmp = new Node<T>();

        // this->constructor( &tmp->data );
        tmp.first_child = null;
        tmp.last_child = null;

        tmp.parent = position.node();
        if (position.node().last_child != null) {
            position.node().last_child.next_sibling = tmp;
        } else {
            position.node().first_child = tmp;
        }
        tmp.prev_sibling = position.node().last_child;
        position.node().last_child = tmp;
        tmp.next_sibling = null;

        return position.newInstance( tmp );
    }
    
    /**
     * 指定された要素を持つノードを指定された反復子の指すノードの子に追加します。
     * @param position 親となるノードを指す反復子
     * @param x 追加されたノードの持つ要素
     * @return 追加されたノードを指す反復子
     */
    public Iterator<T> append_child( Iterator<T> position, final T x )
    {
        //KVS_ASSERT( position.node() != m_head );

        Node<T> tmp = new Node<T>();

        //this->constructor( &tmp->data, x );
        tmp.data = x;
        tmp.first_child = null;
        tmp.last_child  = null;

        tmp.parent = position.node();
        if( position.node().last_child != null )
        {
            position.node().last_child.next_sibling = tmp;
        }
        else
        {
            position.node().first_child = tmp;
        }

        tmp.prev_sibling = position.node().last_child;
        position.node().last_child = tmp;
        tmp.next_sibling = null;

        return position.newInstance( tmp );
    }

    /**
     * 指定された反復子の指すノードの前に指定された要素を持つノードを追加します。
     * @param position 追加する位置を指す反復子
     * @param x 追加されたノードの持つ要素
     * @return 追加されたノードを指す反復子
     */
    public Iterator<T> insert( Iterator<T> position, final T x ) {
        Node<T> tmp = new Node<T>();

        tmp.data = x;
        tmp.first_child = null;
        tmp.last_child = null;

        tmp.parent = position.node().parent;
        tmp.next_sibling = position.node();
        tmp.prev_sibling = position.node().prev_sibling;
        position.node().prev_sibling = tmp;

        if (tmp.prev_sibling == null) {
            tmp.parent.first_child = tmp;
        } else {
            tmp.prev_sibling.next_sibling = tmp;
        }

        return position.newInstance( tmp );
    }

    public SiblingIterator<T> insert( SiblingIterator<T> position, final T x ) {
        Node<T> tmp = new Node<T>();

        tmp.data = x;
        tmp.first_child = null;
        tmp.last_child = null;

        tmp.next_sibling = position.node();
        if (position.node() == null) { // iterator points to end of a subtree
            tmp.parent = position.m_parent;
            tmp.prev_sibling = position.range_last();
        } else {
            tmp.parent = position.node().parent;
            tmp.prev_sibling = position.node().prev_sibling;
            position.node().prev_sibling = tmp;
        }

        if (tmp.prev_sibling == null) {
            tmp.parent.first_child = tmp;
        } else {
            tmp.prev_sibling.next_sibling = tmp;
        }

        return (new SiblingIterator<T>( tmp ));
    }

    /**
     * この木構造が持つノードの数を返します。
     * @return ノードの数
     */
    public final int size() {
        PreOrderIterator<T> it = begin();
        PreOrderIterator<T> eit = end();

        int i = 0;
        while ( !it.equals( eit ) ) {
            ++i;
            it.next();
        }

        return (i);
    }

    /**
     * 指定された反復子の指すノードの深さを返します。
     * @param it 深さを調べるノードを指す反復子
     * @return ノードの深さ
     */
    public final int depth( final Iterator<T> it ) {
        Node<T> pos = it.node();

        int ret = 0;
        while (pos.parent != null) {
            pos = pos.parent;
            ++ret;
        }

        return (ret);
    }

    /**
     * 指定された反復子の指すノードの子の数を返します。
     * @param it 子の数を調べるノードを指す反復子
     * @return 子の数
     */
    public int number_of_children( final Iterator<T> it ) {
        Node<T> pos = it.node().first_child;
        if (pos == null) {
            return (0);
        }

        int ret = 1;
        while ((pos = pos.next_sibling) != null) {
            ++ret;
        }

        return (ret);
    }

    /**
     * 指定された反復子の指すノードの兄弟の数を返します。
     * @param it 子の数を調べるノードを指す反復子
     * @return 兄弟の数
     */
    public int number_of_siblings( final Iterator<T> it ) {
        Node<T> pos = it.node();

        int ret = 1;
        while (pos.next_sibling != null && pos.next_sibling != m_head) {
            ++ret;
            pos = pos.next_sibling;
        }

        return (ret);
    }

    /**
     * 根を初期化します。
     */
    protected void initialize_head() {
        m_head = new Node<T>();

        m_head.parent = null;
        m_head.first_child = null;
        m_head.last_child = null;
        m_head.prev_sibling = m_head;
        m_head.next_sibling = m_head;
    }

    protected Iterator<T> set_head( T x ) {
        // KVS_ASSERT( begin() == end() );

        return (this.insert( this.begin(), x ));
    }

    
    /**
     * この木構造を探索する為の反復子です。
     * 
     * @author eguchi
     */
    public static interface Iterator<T> {
        
        public Iterator<T> begin();

        public Iterator<T> end();

        /**
         * 現在のノードを返します。
         * @return 現在のノード
         */
        public Node<T> node();
        
        /**
         * 現在のノードが持つ要素を返します。
         * @return
         */
        public T getData();
        
        /**
         * 現在のノードの要素を設定します。
         * @param t 設定する要素
         */
        public void setData( T t );

        public void skip_children();

        public int number_of_children();

        /**
         * 繰り返し処理で次の要素を返します。
         * @return　繰り返し処理で次の要素
         */
        public Iterator<T> next();

        /**
         * 繰り返し処理で指定された回数分進んだ先の要素を返します。
         * @param num 進む回数
         * @return　進んだ先の要素
         */
        public Iterator<T> next( int num );

        /**
         * 繰り返し処理で前の要素を返します。
         * @return　繰り返し処理で前の要素
         */
        public Iterator<T> previous();

        /**
         * 繰り返し処理で指定された回数分戻った先の要素を返します。
         * @return　戻った先の要素
         */
        public Iterator<T> previous( int num );

        public Iterator<T> copy();

        public Iterator<T> newInstance( Node<T> node );
    }
    
    /**
     * この木構造を探索するための反復子の基底クラスです。
     * ノードの位置を保持することはできますが探索はできません。
     * @author eguchi
     *
     */
    protected static abstract class IteratorBase<T> implements Iterator<T>{
        protected Node<T> m_node = null; // /< pointer to the node
        protected boolean m_skip_current_children = false; // /< check flag

        public IteratorBase() {
        }

        /**
         * 指定された要素をもつ反復子を返します。
         * @param node
         */
        public IteratorBase( Node<T> node ) {
            m_node = node;
        }

        /**
         * 現在のノードの子供を最初から走査するSiblingIteratorを返します。
         * 
         */
        public final SiblingIterator<T> begin() {
            SiblingIterator<T> ret = new SiblingIterator<T>( m_node.first_child );
            ret.m_parent = m_node;

            return (ret);
        }

        /**
         * 現在のノードの子供を走査する空のSiblingIteratorを返します。
         * 
         */
        public final SiblingIterator<T> end() {
            SiblingIterator<T> ret = new SiblingIterator<T>();
            ret.m_parent = m_node;

            return (ret);
        }

        /**
         * 現在のノードを返します。
         */
        public final Node<T> node() {
            return (m_node);
        }
        
        /**
         * 現在のノードが持つ要素を返します。
         * @return
         */
        public final T getData() {
            return (m_node.data);
        }
        
        /**
         * 現在のノードの要素を設定します。
         * @param t 設定する要素
         */
        public final void setData( T t ) {
            m_node.data = t;
        }

        public void skip_children() {
            m_skip_current_children = true;
        }

        public final int number_of_children() {
            Node<T> p = m_node.first_child;
            if (p == null) {
                return (0);
            }

            int counter = 1;
            while (p != m_node.last_child) {
                ++counter;
                p = p.next_sibling;
            }

            return (counter);
        }

        /**
         * このメソッドは何も行いません。
         * nullを返します。
         */
        public IteratorBase<T> next() {
            return null;

        }

        /**
         * このメソッドは何も行いません。
         * nullを返します。
         */
        public IteratorBase<T> next( int num ) {
            return null;

        }

        /**
         * このメソッドは何も行いません。
         * nullを返します。
         */
        public IteratorBase<T> previous() {
            return null;
        }

        /**
         * このメソッドは何も行いません。
         * nullを返します。
         */
        public IteratorBase<T> previous( int num ) {
            return null;

        }
        
        public boolean equals( final Object obj ){
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Iterator<?>)) {
                return false;
            }
            
            IteratorBase<?> itr = (IteratorBase<?>)obj;
            //return itr.node().equals( this.m_node );
            return itr.m_node == this.m_node;
        }
        
        @Override
        public int hashCode(){
            return 17 + m_node.hashCode();
        }
        
        @Override
        public String toString(){
            return m_node.toString();
        }


    }
    
    /**
     * 幅優先探索を行う反復子です。
     * @author electra
     */
    protected static class BreadthFirstIterator<T> extends IteratorBase<T> {

        private LinkedList<Node<T>> m_node_queue;

        /**
         * 空の反復子を返します。
         */
        public BreadthFirstIterator() {
            super();
        }

        /**
         * 指定されたノードを起点とする反復子を返します。
         * @param t 起点となるノード
         */
        public BreadthFirstIterator( Node<T> t ) {
            super( t );
            m_node_queue.add( t );
        }

        /**
         * 指定された反復子が現在指しているノードを起点とする反復子を返します。
         * @param other 起点となるノードを指す反復子
         */
        public BreadthFirstIterator( final IteratorBase<T> other ) {
            super( other.node() );
            m_node_queue.add( other.node() );
        }
 
        /**
         * 繰り返し処理で次の要素を返します。
         * @return　繰り返し処理で次の要素
         */
        @Override
        public IteratorBase<T> next() {
            SiblingIterator<T> sibling = this.begin();
            while( sibling != this.end() )
            {
                m_node_queue.add( sibling.node() );
                sibling.next();
            }

            m_node_queue.removeFirst();
            if( m_node_queue.size() > 0 )
            {
                m_node = m_node_queue.getFirst();
            }
            else
            {
                m_node = null;
            }

            return( this );

        }

        /**
         * 繰り返し処理で指定された回数分進んだ先の要素を返します。
         * @param num 進む回数
         * @return　進んだ先の要素
         */
        @Override
        public IteratorBase<T> next( int num ) {
            while (num > 0) {
                this.next();
                --num;
            }
            return this;

        }

        @Override
        public Iterator<T> newInstance( Node<T> node ) {
            return new BreadthFirstIterator<T>( node );
        }

        @Override
        public Iterator<T> copy() {
            BreadthFirstIterator<T> copy = new BreadthFirstIterator<T>( m_node );
            copy.m_skip_current_children = this.m_skip_current_children;
            copy.m_node_queue.clear();
            for( Node<T> node : this.m_node_queue ){
                copy.m_node_queue.add( node );      //deep copy
            }
            return copy;
        }

    }

    /**
     * 後順走査を行う反復子です。
     * @author eguchi
     */
    protected static class PostOrderIterator<T> extends IteratorBase<T> {

        public PostOrderIterator( Node<T> t ) {
            super( t );
        }

        public PostOrderIterator( final IteratorBase<T> other ) {
            super( other.node() );
        }

        public PostOrderIterator( final SiblingIterator<T> other )
        {
            super( other.node() );
            if( m_node == null )
            {
                if( other.range_last() != null ){
                    m_node = other.range_last();
                }
                else{
                    m_node = other.m_parent;
                }
                this.skip_children();
                this.next();
            }
        }

        public void descend_all() {
            // KVS_ASSERT( m_node != 0 );

            while (m_node.first_child != null) {
                m_node = m_node.first_child;
            }
        }
        
        @Override
        public IteratorBase<T> next() {
            if( m_node.next_sibling == null )
            {
                m_node = m_node.parent;
            }
            else
            {
                m_node = m_node.next_sibling;
                if( m_skip_current_children )
                {
                    m_skip_current_children = false;
                }
                else
                {
                    while( m_node.first_child != null){
                        m_node = m_node.first_child;
                    }
                }
            }

            return( this );

        }

        @Override
        public IteratorBase<T> next( int num ) {
            while (num > 0) {
                this.next();
                --num;
            }
            return this;

        }

        @Override
        public IteratorBase<T> previous() {
            if( m_skip_current_children || m_node.last_child == null )
            {
                m_skip_current_children = false;

                while( m_node.prev_sibling == null ){
                    m_node = m_node.parent;
                }
                m_node = m_node.prev_sibling;
            }
            else
            {
                m_node = m_node.last_child;
            }

            return( this );
        }

        @Override
        public IteratorBase<T> previous( int num ) {
            while (num > 0) {
                this.previous();
                --num;
            }
            return this;

        }

        @Override
        public Iterator<T> newInstance( Node<T> node ) {
            return new PostOrderIterator<T>( node );
        }

        @Override
        public Iterator<T> copy() {
            PostOrderIterator copy = new PostOrderIterator<T>( this.m_node );
            copy.m_skip_current_children = this.m_skip_current_children;
            return copy;
        }
    }
    
    /**
     * 前順走査を行う反復子です。
     * @author eguchi
     */
    protected static class PreOrderIterator<T> extends IteratorBase<T> {

        public PreOrderIterator( Node<T> node ) {
            super( node );
        }

        public PreOrderIterator( final IteratorBase<T> other ) {
            super( other.node() );
        }

        public PreOrderIterator( final SiblingIterator<T> other )
        {
            super( other.node() );
            if( m_node == null )
            {
                if( other.range_last() != null )
                {
                    m_node = other.range_last();
                }
                else
                {
                    m_node = other.m_parent;
                }

                this.skip_children();
                this.next();
            }
        }

        /**
         * 繰り返し処理で次の要素を返します。
         * @return　繰り返し処理で次の要素
         */
        @Override
        public IteratorBase<T> next() {
            if( !m_skip_current_children &&
                    m_node.first_child != null )
            {
                m_node = m_node.first_child;
            }
            else
            {
                m_skip_current_children = false;

                while( m_node.next_sibling == null )
                {
                    m_node = m_node.parent;
                    if( m_node == null ){
                        return( this );
                    }
                }
                m_node = m_node.next_sibling;
            }

            return( this );

        }

        /**
         * 繰り返し処理で指定された回数分進んだ先の要素を返します。
         * @param num 進む回数
         * @return　進んだ先の要素
         */
        @Override
        public IteratorBase<T> next( int num ) {
            while (num > 0) {
                this.next();
                --num;
            }
            return this;

        }

        /**
         * 繰り返し処理で前の要素を返します。
         * @return　繰り返し処理で前の要素
         */
        @Override
        public IteratorBase<T> previous() {
            if( m_node.prev_sibling == null )
            {
                m_node = m_node.prev_sibling;
                while( m_node.last_child == null )
                {
                    m_node = m_node.last_child;
                }
            }
            else
            {
                m_node = m_node.parent;
                if( m_node == null ){
                    return( this );
                }
            }

            return( this );
        }

        /**
         * 繰り返し処理で指定された回数分戻った先の要素を返します。
         * @return　戻った先の要素
         */
        @Override
        public IteratorBase<T> previous( int num ) {
            while (num > 0) {
                this.previous();
                --num;
            }
            return this;

        }

        @Override
        public Iterator<T> newInstance( Node<T> node ) {
            return new PreOrderIterator<T>( node );
        }

        @Override
        public Iterator<T> copy() {
            PreOrderIterator copy = new PreOrderIterator<T>( this.m_node );
            copy.m_skip_current_children = this.m_skip_current_children;
            return copy;
        }
    }
    
    /**
     * 兄弟ノードを走査する反復子です。
     */
    protected static class SiblingIterator<T> extends IteratorBase<T> {
        
        public Node<T> m_parent;

        /**
         * 空の反復子を返します。
         */
        public SiblingIterator() {
            super();
        }

        /**
         * 指定されたノードを起点とする反復子を返します。
         * @param t 起点となるノード
         */
        public SiblingIterator( Node<T> t ) {
            super( t );
            this.set_parent();
        }

        /**
         * 指定されたSiblingIteratorの複製を返します。
         * @param other 複製するSiblingIterator
         */
        public SiblingIterator( final SiblingIterator<T> other ) {
            super( other.node() );
            m_parent = other.m_parent;
        }

        /**
         * 指定されたSiblingIterator以外の反復子が現在指しているノードの兄弟を走査する
         * 反復子を返します。
         * @param other 
         */
        public SiblingIterator( final IteratorBase<T> other ) {
            super( other.node() );
            this.set_parent();
        }

        /**
         * このノードの兄弟の内で最初のノードを返します。
         * @return 兄弟の内で最初のノード
         */
        public final Node<T> range_first() {
            Node<T> tmp = m_parent.first_child;
            return (tmp);
        }

        /**
         * このノードの兄弟の内で最後のノードを返します。
         * @return 兄弟の内で最後のノード
         */
        public final Node<T> range_last() {
            return (m_parent.last_child);
        }

        private void set_parent() {
            m_parent = null;

            if (m_node == null) {
                return;
            }
            if (m_node.parent != null) {
                m_parent = m_node.parent;
            }
        }
        
        /**
         * 繰り返し処理で次の要素を返します。
         * @return　繰り返し処理で次の要素
         */
        @Override
        public IteratorBase<T> next() {
            if( m_node != null ){
                m_node = m_node.next_sibling;
            }

            return( this );

        }

        
        /**
         * 繰り返し処理で指定された回数分進んだ先の要素を返します。
         * @param num 進む回数
         * @return　進んだ先の要素
         */
        @Override
        public IteratorBase<T> next( int num ) {
            while (num > 0) {
                this.next();
                --num;
            }
            return this;

        }

        /**
         * 繰り返し処理で前の要素を返します。
         * @return　繰り返し処理で前の要素
         */
        @Override
        public IteratorBase<T> previous() {
            if( m_node != null )
            {
                m_node = m_node.prev_sibling;
            }
            else
            {
                //KVS_ASSERT( m_parent != 0 );

                m_node = m_parent.last_child;
            }

            return( this );
        }

        /**
         * 繰り返し処理で指定された回数分戻った先の要素を返します。
         * @return　戻った先の要素
         */
        @Override
        public IteratorBase<T> previous( int num ) {
            while (num > 0) {
                this.previous();
                --num;
            }
            return this;

        }

        @Override
        public Iterator<T> newInstance( Node<T> node ) {
            return new SiblingIterator<T>( node );
        }

        @Override
        public Iterator<T> copy() {
            SiblingIterator copy = new SiblingIterator<T>( this.m_node  );
            copy.m_skip_current_children = this.m_skip_current_children;
            copy.m_parent = this.m_parent;
            return copy;
        }
    }
    
    /**
     * ツリーのノードとなるクラスです。
     */
    protected static class Node<T> {
        
        public Node<T> parent; // /< pointer to the parent node
        public Node<T> first_child; // /< pointer to the first child node
        public Node<T> last_child; // /< pointer to the last child node
        public Node<T> prev_sibling; // /< pointer to the previous sibling node
        public Node<T> next_sibling; // /< pointer to the next sibling node
        public T data; // /< node data
        
        @Override
        public boolean equals( final Object obj ){
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Node<?>)) {
                return false;
            }
            
            Node<?> node = (Node<?>)obj;
            if ( node.parent == this.parent
                    && node.first_child == this.first_child
                    && node.last_child == this.last_child
                    && node.prev_sibling == this.prev_sibling
                    && node.next_sibling == this.next_sibling
                    && data.equals( data )){
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode(){
            return 17 + data.hashCode();
        }
        
        @Override
        public String toString(){
            return data.toString();
        }

    }
    
    /**
     * 最後尾のリーフのみの走査を行う反復子です。
     * @author eguchi
     */
    protected static class LeafIterator<T> extends IteratorBase<T> {

        public LeafIterator() {
            super();
        }

        /**
         * 指定されたノードを起点とする反復子を返します。
         * @param t 起点となるノード
         */
        public LeafIterator( Node<T> t ) {
            super( t );
        }

        /**
         * 
         * @param other 
         */
        public LeafIterator( final SiblingIterator<T> other )
        {
            super( other.node() );
            if( m_node == null )
            {
                if( other.range_last() != null )
                {
                    m_node = other.range_last();
                }
                else
                {
                    m_node = other.m_parent;
                }

                this.next();
            }
        }

        /**
         * 指定されたSiblingIterator以外の反復子が現在指しているノードを起点とする反復子を返します。
         * @param other 
         */
        public LeafIterator( final IteratorBase<T> other ) {
            super( other.node() );
        }
        
        /**
         * 繰り返し処理で次の要素を返します。
         * @return　繰り返し処理で次の要素
         */
        @Override
        public IteratorBase<T> next() {
            while( m_node.next_sibling == null )
            {
                if( m_node.parent == null ){
                    return( this );
                }
                m_node = m_node.parent;
            }

            m_node = m_node.next_sibling;

            while( m_node.last_child != null )
            {
                m_node = m_node.first_child;
            }
            
            return ( this );

        }

        /**
         * 繰り返し処理で指定された回数分進んだ先の要素を返します。
         * @param num 進む回数
         * @return　進んだ先の要素
         */
        @Override
        public IteratorBase<T> next( int num ) {
            while (num > 0) {
                this.next();
                --num;
            }
            return this;

        }

        /**
         * 繰り返し処理で指定された回数分戻った先の要素を返します。
         * @return　戻った先の要素
         */
        @Override
        public IteratorBase<T> previous() {
            while( m_node.prev_sibling == null )
            {
                if( m_node.parent == null ){
                    return( this );
                }
                m_node = m_node.parent;
            }

            m_node = m_node.prev_sibling;

            while( m_node.last_child != null )
            {
                m_node = m_node.last_child;
            }

            return( this );
        }

        /**
         * 繰り返し処理で指定された回数分戻った先の要素を返します。
         * @return　戻った先の要素
         */
        @Override
        public IteratorBase<T> previous( int num ) {
            while (num > 0) {
                this.previous();
                --num;
            }
            return this;

        }

        @Override
        public Iterator<T> newInstance( Node<T> node ) {
            return new LeafIterator<T>( node );
        }

        @Override
        public Iterator<T> copy() {
            LeafIterator copy = new LeafIterator<T>( this.m_node  );
            copy.m_skip_current_children = this.m_skip_current_children;
            return copy;
        }

    }





}
